package com.zjh.gmall.admin.ums.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zjh.gmall.to.CommonResult;
import com.zjh.gmall.ums.entity.MemberLevel;
import com.zjh.gmall.ums.service.MemberLevelService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by 24923 on 2019/12/25 12:07
 */
@CrossOrigin
@RestController
public class UmsMemberLevelController {

    @Reference
    MemberLevelService memberLevelService;
    /*
    * 查出所有会员等级
    * */
    @GetMapping("/memberLevel/list")
    public CommonResult memberLevelList(){
        List<MemberLevel> list = memberLevelService.list();
        return new CommonResult().success(list);
    }
}
