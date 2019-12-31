package com.zjh.gmall.pms;

import com.zjh.gmall.pms.entity.Brand;
import com.zjh.gmall.pms.entity.Product;
import com.zjh.gmall.pms.service.BrandService;
import com.zjh.gmall.pms.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;


@SpringBootTest
class GmallPmsApplicationTests {

    @Autowired
    ProductService productService;

    @Autowired
    BrandService brandService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedisTemplate<Object,Object> redisTemplateObj;
    @Test
    void contextLoads() {
//        Product byId = productService.getById(2);
//        System.out.println(byId.getName());
        //测试增删改在主库，查在从库
//        Brand brand = new Brand();
//        brand.setName("哈哈哈");
//        brandService.save(brand);
        Brand byId = brandService.getById(53);

        System.out.println("保存成功。。。"+byId.getName());

    }

    @Test
    public void redisTemplate(){

//        redisTemplate.opsForValue();//操作redis中string类型的
//        redisTemplate.opsForHash();//操作redis中hash类型的
//        redisTemplate.opsForList();//操作redis中list类型的

        redisTemplate.opsForValue().set("hello","world");
        System.out.println("保存了数据");

        String hello = redisTemplate.opsForValue().get("hello");
        System.out.println("刚才保存的值是："+hello);

    }

    @Test
    public void setRedisTemplateObj(){
        //以后要存对象将对象转化为json字符串
        //去redis中取出来，逆转为对象
        Brand brand = new Brand();
        brand.setName("啊哈哈哈");
        redisTemplateObj.opsForValue().set("abc",brand);

        System.out.println("刚存了一个对象");


        Brand abc = (Brand) redisTemplateObj.opsForValue().get("abc");
        System.out.println("刚才保存的对象值是："+abc.getName());
    }

}
