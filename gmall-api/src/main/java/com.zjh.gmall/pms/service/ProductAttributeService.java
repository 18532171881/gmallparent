package com.zjh.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjh.gmall.pms.entity.ProductAttribute;
import com.zjh.gmall.vo.PageInfoVo;

/**
 * <p>
 * 商品属性参数表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-12-22
 */
public interface ProductAttributeService extends IService<ProductAttribute> {

    /*查询某个属性分类下的所有属性和参数
    * */
    PageInfoVo getCategoryAttributes(Long cid, Integer type, Integer pageSize, Integer pageNum);
}
