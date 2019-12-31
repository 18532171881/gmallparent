package com.zjh.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjh.gmall.constant.EsConstant;
import com.zjh.gmall.pms.entity.*;
import com.zjh.gmall.pms.mapper.*;
import com.zjh.gmall.pms.service.ProductService;
import com.zjh.gmall.to.es.EsProduct;
import com.zjh.gmall.to.es.EsProductAttributeValue;
import com.zjh.gmall.to.es.EsSkuProductInfo;
import com.zjh.gmall.vo.PageInfoVo;
import com.zjh.gmall.vo.product.PmsProductParam;
import com.zjh.gmall.vo.product.PmsProductQueryParam;
import io.searchbox.client.JestClient;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-12-21
 */
@Slf4j
@Service
@Component
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductAttributeValueMapper productAttributeValueMapper;

    @Autowired
    ProductFullReductionMapper productFullReductionMapper;

    @Autowired
    ProductLadderMapper productLadderMapper;

    @Autowired
    SkuStockMapper skuStockMapper;

    @Autowired
    JestClient jestClient;
    //当前线程共享同样的数据
    ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    @Override
    public Product productInfo(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public PageInfoVo productPageInfo(PmsProductQueryParam param) {

        QueryWrapper<Product> wrapper = new QueryWrapper<>();

        if(param.getBrandId()!=null){
            //前端传了
            wrapper.eq("brand_id",param.getBrandId());
        }

        if(!StringUtils.isEmpty(param.getKeyword())){
            wrapper.like("name",param.getKeyword());
        }

        if(param.getProductCategoryId()!=null){
            wrapper.eq("product_category_id",param.getProductCategoryId());
        }

        if(!StringUtils.isEmpty(param.getProductSn())){
            wrapper.like("product_sn",param.getProductSn());
        }

        if(param.getPublishStatus()!=null){
            wrapper.eq("publish_status",param.getPublishStatus());
        }

        if(param.getVerifyStatus()!=null){
            wrapper.eq("verify_status",param.getVerifyStatus());
        }


        IPage<Product> page = productMapper.selectPage(new Page<Product>(param.getPageNum(), param.getPageSize()),wrapper);

        PageInfoVo pageInfoVo = new PageInfoVo(page.getTotal(),page.getPages(),param.getPageSize(),
                page.getRecords(),page.getCurrent());

        return pageInfoVo;
    }
/*
* 大保存
* */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveProduct(PmsProductParam productParam) {
        ProductServiceImpl proxy = (ProductServiceImpl) AopContext.currentProxy();

        //保存基本信息
        proxy.saveBaseInfo(productParam);

        //5、pms_sku_stock sku库存表
        proxy.saveSkuStock(productParam);

        /*
        * 以下都可以try-cache互不影响
        * */
        //2、pms_product_attribute_value保存这个商品队形的所有属性的值
        try{
            proxy.saveProductAttributeValue(productParam);
        }catch (Exception e){
            e.printStackTrace();
        }

        //3、pms_product_full_reduction保存商品的满减信息
        try{
            proxy.saveProductFullReduction(productParam);
        }catch (Exception e){
            e.printStackTrace();
        }


        //4、pms_product_ladder阶梯价格表
        try{
            proxy.saveProductLadder(productParam);
        }catch (Exception e){
             e.printStackTrace();
        }

    }

    /*
    * 大删除
    * */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteProduct(List<Long> ids) {
        ProductServiceImpl proxy = (ProductServiceImpl) AopContext.currentProxy();
        try{
        proxy.deleteProductAttributeValue(ids);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            proxy.deleteProductFullReduction(ids);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            proxy.deleteProductLadder(ids);
        }catch (Exception e){
            e.printStackTrace();
        }
        proxy.deleteSkuStock(ids);
        proxy.deleteBase(ids);

    }

    @Override
    public void updatePublishStatus(List<Long> ids, Integer publishStatus) {

        if(publishStatus == 0){
            ids.forEach((id)->{
                //下架
                //该数据库状态
                setProductPublishStatus(publishStatus, id);

                //删es
                deleteProductFromEs(id);
            });
        }else {
            //上架
            ids.forEach((id)->{
                setProductPublishStatus(publishStatus, id);
                //改数据库状态
                //加es
                saveProductToEs(id);
            });

        }
//

    }

    private void deleteProductFromEs(Long id) {
        Delete delete = new Delete.Builder(id.toString())
                .index(EsConstant.PRODUCT_ES_INDEX)
                .type(EsConstant.PRODUCT_INFO_ES_TYPE)
                .build();

        try{
            DocumentResult execute = jestClient.execute(delete);
            if(execute.isSucceeded()){
                log.info("商品：{}==>ES下架成功",id);
            }else {
//                deleteProductFromEs(id);
                log.error("商品：{}==>ES下架失败",id);
            }

        }catch (Exception e){
//                deleteProductFromEs(id);
            log.error("商品：{}==>ES下架失败",id);
        }
    }

    private void saveProductToEs(Long id) {
        Product productInfo = productInfo(id);


        EsProduct esProduct = new EsProduct();
        BeanUtils.copyProperties(productInfo,esProduct);

        List<SkuStock> stocks = skuStockMapper.selectList(new QueryWrapper<SkuStock>().eq("product_id", id));
        List<EsSkuProductInfo> esSkuProductInfos=new ArrayList<>(stocks.size());

        //查出当前商品的sku属性
        List<ProductAttribute> skuAttributeNames= productAttributeValueMapper.selectProductSaleAttrName(id);
        stocks.forEach((skuStock)->{
            EsSkuProductInfo info = new EsSkuProductInfo();
            BeanUtils.copyProperties(skuStock,info);

            //拼写sku的特色标题
            String subTitle =esProduct.getName();
            if(!StringUtils.isEmpty(skuStock.getSp1())){
                subTitle+=" "+skuStock.getSp1();
            }
            if(!StringUtils.isEmpty(skuStock.getSp2())){
                subTitle+=" "+skuStock.getSp2();
            }
            if(!StringUtils.isEmpty(skuStock.getSp3())){
                subTitle+=" "+skuStock.getSp3();
            }
            info.setSkuTitle(subTitle);


            List<EsProductAttributeValue> skuAttributeValues=new ArrayList<>();
            for (int i=0;i<skuAttributeNames.size();i++){
                EsProductAttributeValue value=new EsProductAttributeValue();

                value.setName(skuAttributeNames.get(i).getName());
                value.setProductId(id);
                value.setProductAttributeId(skuAttributeNames.get(i).getId());
                value.setType(skuAttributeNames.get(i).getType());

                //颜色 尺码，让es去统计；改掉查询商品的属性分类里面所有属性的时候，按照sort字段排序号
                if (i==0){
                    value.setValue(skuStock.getSp1());
                }
                if (i==1){
                    value.setValue(skuStock.getSp2());
                }
                if (i==2){
                    value.setValue(skuStock.getSp3());
                }
                skuAttributeValues.add(value);
            }
            info.setAttributeValues(skuAttributeValues);

            esSkuProductInfos.add(info);

        });
        esProduct.setSkuProductInfos(esSkuProductInfos);

        //公共属性
        List<EsProductAttributeValue> attributeValues=productAttributeValueMapper.selectProductBaseAttrAndValue(id);
        //复制公共属性信息，查出这个商品的公共属性
        esProduct.setAttrValueList(attributeValues);

        try{
            //把商品保存到es中
            Index build = new Index.Builder(esProduct)
                    .index(EsConstant.PRODUCT_ES_INDEX)
                    .type(EsConstant.PRODUCT_INFO_ES_TYPE)
                    .id(id.toString())
                    .build();
            DocumentResult execute = jestClient.execute(build);
            boolean succeeded = execute.isSucceeded();
            if(succeeded){
                log.info("ES中；id为{}商品上架完成",id);
            }else {
                log.error("ES中；id为{}商品未保存成功，开始重试",id);
//                saveProductToEs(id);
            }
        }catch (Exception e){
            log.error("ES中；id为{}商品数据保存异常 {}",id,e.getMessage());
            //                saveProductToEs(id);
        }


    }

    public void setProductPublishStatus(Integer publishStatus, Long id) {
        Product product = new Product();
        product.setId(id);
        product.setPublishStatus(publishStatus);
        productMapper.updateById(product);
    }

    @Override
    public void updateNewsStatus(List<Long> ids, Integer newStatus) {
        Product product = new Product();
        product.setNewStatus(newStatus);
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        for (int i=0;i<ids.size();i++){
            wrapper.eq("id",ids.get(i));
        }
        productMapper.update(product,wrapper);
    }

    @Override
    public void updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        Product product = new Product();
        product.setRecommandStatus(recommendStatus);
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        for (int i=0;i<ids.size();i++){
            wrapper.eq("id",ids.get(i));
        }
        productMapper.update(product,wrapper);
    }

    public void deleteBase(List<Long> ids) {
        for (int i=0;i<ids.size();i++){
            QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
            QueryWrapper<Product> id = productQueryWrapper.eq("id", ids.get(i));
            productMapper.delete(id);
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteProductLadder(List<Long> ids) {
        for (int i=0;i<ids.size();i++){
            QueryWrapper<ProductLadder> plwrapper = new QueryWrapper<>();
            QueryWrapper<ProductLadder> pl = plwrapper.eq("product_id", ids.get(i));
            productLadderMapper.delete(pl);
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteProductFullReduction(List<Long> ids) {
        for (int i=0;i<ids.size();i++){
            QueryWrapper<ProductFullReduction> pfrwrapper = new QueryWrapper<>();
            QueryWrapper<ProductFullReduction> pfr = pfrwrapper.eq("product_id", ids.get(i));
            productFullReductionMapper.delete(pfr);
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteProductAttributeValue(List<Long> ids) {
        for (int i=0;i<ids.size();i++){
            QueryWrapper<ProductAttributeValue> pavwrapper=new QueryWrapper<>();
            QueryWrapper<ProductAttributeValue> pav = pavwrapper.eq("product_id", ids.get(i));
            productAttributeValueMapper.delete(pav);
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteSkuStock(List<Long> ids) {
        for (int i=0;i<ids.size();i++){
            QueryWrapper<SkuStock> skwrapper =new QueryWrapper<>();
            QueryWrapper<SkuStock> sk = skwrapper.eq("product_id", ids.get(i));
            skuStockMapper.delete(sk);
        }

    }




    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSkuStock(PmsProductParam productParam) {
        List<SkuStock> skuStockList = productParam.getSkuStockList();
        for (int i=1; i<=skuStockList.size(); i++){
            SkuStock skuStock = skuStockList.get(i-1);
            if (StringUtils.isEmpty(skuStock.getSkuCode())){
                //skuCode必须有
                //生成规则 商品id_sku自增id
                skuStock.setSkuCode(threadLocal.get()+"_"+i);
            }
            skuStock.setProductId(threadLocal.get());
            skuStockMapper.insert(skuStock);
        }
    }

    /*保存商品基础信息*/
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveBaseInfo(PmsProductParam productParam){
        //1、pms_product保存商品基本信息
        Product product=new Product();
        BeanUtils.copyProperties(productParam,product);
        productMapper.insert(product);
        //mybaits-plus能自动获取到刚才这个数据的自增id
        threadLocal.set(product.getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductAttributeValue(PmsProductParam productParam) {
        List<ProductAttributeValue> valueList = productParam.getProductAttributeValueList();
        valueList.forEach((item)->{
            item.setProductId(threadLocal.get());
            productAttributeValueMapper.insert(item);
        });
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductLadder(PmsProductParam productParam) {
        List<ProductLadder> ladderList = productParam.getProductLadderList();
        ladderList.forEach((productLadder)->{
            productLadder.setProductId(threadLocal.get());
            productLadderMapper.insert(productLadder);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductFullReduction(PmsProductParam productParam) {
        List<ProductFullReduction> fullReductionList = productParam.getProductFullReductionList();
        fullReductionList.forEach((reduction)->{
            reduction.setProductId(threadLocal.get());
            productFullReductionMapper.insert(reduction);
        });
    }





}
