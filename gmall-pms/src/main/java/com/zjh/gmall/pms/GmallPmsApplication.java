package com.zjh.gmall.pms;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
* Logstach整合
* 1、倒jar包
* 2、倒日志配置
*
*
* 缓存的使用场景
* 一些固定的数据，不太变化的数据，高频访问的数据（基本不变），变化频路低的都可以如缓存，加速系统的访问
* 缓存的目的：提高系统查询效率，提供性能
*
* 1）、将菜单缓存起来，以后查询直接去缓存中拿即可
*
*
*
* 设计模式：模板模式
* 操作xxx都有对应的xxxTemplate：
* JdbcTemplate、RestTemplate、RedisTemplate
*
*
* 整合Redis两大步
*   1导入starter-data-redis
*   2application。properties配置与 Spring。redis相关的
*   注意：
*       RedisTemplate：存数据默认使用jdk的方式序列化存过去
* 我们推荐都推荐应该存成json：
*   做法：
*       将默认的序列化器改为json的
*
* 如果发信失误加不上，开启基于注解的事物功能 @EnableTransactionManagement
*
*
* 3 事物的最终解决方案
*       1；普通加事物 导入jdbc-starter @EnableTransactionManagement  加上@Transational
*       2；方法自己调用自己类里面的加不上事物
*               1、导入aop包，开启事物对象的相关功能
*               <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
        *
        * 2、获取到当前真正的代理对象，去掉方法即可
        * 1).@EnableAspectJAutoProxy(exposeProxy = true 暴露代理对象
        * 2).获取代理对象
        *
        *
        * 异常的回滚策略
        * 运行时异常不受检查异常）
        *       ArithmeticException。。。
        * 编译时异常（受检查异常）
        *       FileNotFound 要么throw 要么try-cache
        * 运行时的异常默认是一定回滚的
        *
        * 编译时异常默认是不会滚的
        *   rollbackFor制定呢些异常一定回滚
        *   norollbackFor制定呢些异常不会滚
* */
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableTransactionManagement
@EnableDubbo
@MapperScan(basePackages = "com.zjh.gmall.pms.mapper")
@SpringBootApplication
public class GmallPmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallPmsApplication.class, args);
    }

}
