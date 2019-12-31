package com.zjh.gmall.ums.service.impl;

import com.zjh.gmall.ums.entity.Member;
import com.zjh.gmall.ums.mapper.MemberMapper;
import com.zjh.gmall.ums.service.MemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-12-21
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

}
