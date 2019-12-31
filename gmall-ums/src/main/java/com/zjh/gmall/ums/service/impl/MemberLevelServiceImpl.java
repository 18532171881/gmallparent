package com.zjh.gmall.ums.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjh.gmall.ums.entity.MemberLevel;
import com.zjh.gmall.ums.mapper.MemberLevelMapper;
import com.zjh.gmall.ums.service.MemberLevelService;
import org.springframework.stereotype.Component;


/**
 * <p>
 * 会员等级表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-12-21
 */
@Service
@Component
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelMapper, MemberLevel> implements MemberLevelService {

}
