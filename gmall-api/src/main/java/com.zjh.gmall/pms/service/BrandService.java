package com.zjh.gmall.pms.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjh.gmall.pms.entity.Brand;
import com.zjh.gmall.vo.PageInfoVo;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 品牌表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-12-22
 */
public interface BrandService extends IService<Brand> {

    PageInfoVo brandPageInfo(String keyword, Integer pageNum, Integer pageSize);
}
