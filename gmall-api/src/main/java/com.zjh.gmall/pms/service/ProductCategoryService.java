package com.zjh.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjh.gmall.pms.entity.ProductCategory;
import com.zjh.gmall.vo.product.PmsProductCategoryWithChildrenItem;

import java.util.List;

/**
 * <p>
 * 产品分类 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-12-22
 */
public interface ProductCategoryService extends IService<ProductCategory> {

    List<PmsProductCategoryWithChildrenItem> listCatelogWithChilder(Integer i);
}
