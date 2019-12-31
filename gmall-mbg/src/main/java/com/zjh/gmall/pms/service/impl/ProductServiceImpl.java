package com.zjh.gmall.pms.service.impl;

import com.zjh.gmall.pms.entity.Product;
import com.zjh.gmall.pms.mapper.ProductMapper;
import com.zjh.gmall.pms.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-12-24
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
