package com.zjh.gmall.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/*
* VO（View Object/Value Object）视图对象
* DAO:(Database Access Object)数据库访问对象，专门用来对数据库进行crud操作的对象。xxxMapper
* POJO(Plain Old Java Object)古老的单纯的java对象，javaBean（封装数据的）
* DO（Data Object）数据对象 数据库对象（专门用来封装数据表的实体类）
* TO（Transfer Object）传输对象（）
* DTO==TO
*
*
* @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
* 不进行数据源的自动配置
*
* 如果导入的依赖，引入一个自动配置长江
* 1.这个场景自动配置默认生效，我们就必须配置他
* 2.排除掉这个场景的自动配置类
* */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GmallAdminWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallAdminWebApplication.class, args);
    }

}
