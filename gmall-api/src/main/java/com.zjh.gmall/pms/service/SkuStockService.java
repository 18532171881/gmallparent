package com.zjh.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjh.gmall.pms.entity.SkuStock;

import java.util.List;

/**
 * <p>
 * sku的库存 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-12-22
 */
public interface SkuStockService extends IService<SkuStock> {

    List<SkuStock> searchsku(Long pid,String keyword);

    String updateforProductid(Long pid, List<SkuStock> skuStockList);
}
