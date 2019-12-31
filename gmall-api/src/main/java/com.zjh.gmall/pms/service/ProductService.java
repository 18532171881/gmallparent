package com.zjh.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;


import com.zjh.gmall.pms.entity.Product;
import com.zjh.gmall.vo.PageInfoVo;
import com.zjh.gmall.vo.product.PmsProductParam;
import com.zjh.gmall.vo.product.PmsProductQueryParam;

import java.util.List;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-12-22
 */
public interface ProductService extends IService<Product> {

    Product productInfo(Long id);

    PageInfoVo productPageInfo(PmsProductQueryParam productQueryParam);

    /*
    * 保存商品数据
    * */
    void saveProduct(PmsProductParam productParam);

    void deleteProduct(List<Long> ids);

    void updatePublishStatus(List<Long> ids, Integer publishStatus);

    void updateNewsStatus(List<Long> ids, Integer newStatus);

    void updateRecommendStatus(List<Long> ids, Integer recommendStatus);
}
