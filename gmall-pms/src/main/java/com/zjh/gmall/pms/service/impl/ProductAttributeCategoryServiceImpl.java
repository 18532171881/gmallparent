package com.zjh.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjh.gmall.pms.entity.ProductAttributeCategory;
import com.zjh.gmall.pms.mapper.ProductAttributeCategoryMapper;
import com.zjh.gmall.pms.service.ProductAttributeCategoryService;
import com.zjh.gmall.vo.PageInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 产品属性分类表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-12-21
 */
@Component
@Service
public class ProductAttributeCategoryServiceImpl extends ServiceImpl<ProductAttributeCategoryMapper, ProductAttributeCategory> implements ProductAttributeCategoryService {

    @Autowired
    ProductAttributeCategoryMapper productAttributeCategoryMapper;
    @Override
    public PageInfoVo productAttributeCateGoryPageInfo(Integer pageNum, Integer pageSize) {

        IPage<ProductAttributeCategory> page = productAttributeCategoryMapper.selectPage(new Page<ProductAttributeCategory>(pageNum, pageSize), null);


        return PageInfoVo.getVo(page,pageSize.longValue());
    }
}

