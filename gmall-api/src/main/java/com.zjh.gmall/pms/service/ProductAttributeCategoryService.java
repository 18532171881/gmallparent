package com.zjh.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjh.gmall.pms.entity.ProductAttributeCategory;
import com.zjh.gmall.vo.PageInfoVo;

/**
 * <p>
 * 产品属性分类表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-12-22
 */
public interface ProductAttributeCategoryService extends IService<ProductAttributeCategory> {
/*
* 分页查询所有属性分类
* */
    PageInfoVo productAttributeCateGoryPageInfo(Integer pageNum, Integer pageSize);
}
