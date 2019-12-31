package com.zjh.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjh.gmall.pms.entity.SkuStock;
import com.zjh.gmall.pms.mapper.SkuStockMapper;
import com.zjh.gmall.pms.service.SkuStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * sku的库存 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-12-21
 */
@Service
@Component
public class SkuStockServiceImpl extends ServiceImpl<SkuStockMapper, SkuStock> implements SkuStockService {

    @Autowired
    SkuStockMapper skuStockMapper;
    @Override
    public List<SkuStock> searchsku(Long pid,String keyword) {
        QueryWrapper<SkuStock> wrapper = new QueryWrapper<>();

        wrapper.eq("product_id", pid);

        if(keyword!=null){
            wrapper.like("sku_code",keyword);
        }
        List<SkuStock> skuStocks = skuStockMapper.selectList(wrapper);
        return skuStocks;
    }

    @Override
    public String updateforProductid(Long pid, List<SkuStock> skuStockList) {

        for (int i=0;i<skuStockList.size();i++){

            QueryWrapper<SkuStock> wrapper = new QueryWrapper<>();
            wrapper.eq("product_id",pid);
            wrapper.eq("sku_code",skuStockList.get(i).getSkuCode());
            skuStockMapper.update(skuStockList.get(i),wrapper);
        }

        return "修改成功";
    }
}
