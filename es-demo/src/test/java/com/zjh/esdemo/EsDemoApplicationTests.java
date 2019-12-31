package com.zjh.esdemo;

import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsDemoApplicationTests {


    @Autowired
    JestClient jestClient;

    @Test
    public void contextLoads() {
        System.out.println(jestClient);

    }
    @Test
    public void index() throws IOException {

        User user = new User();
        user.setEmail("qq.com");
        user.setUserName("zhangsan");
        Index index = new Index.Builder(user)
                .index("user")
                .type("info")
                .build();
        DocumentResult execute = jestClient.execute(index);
        System.out.println("执行？"+execute.getId()+"==>"+execute.isSucceeded());
    }

}
class User{
    private String userName;
    private String email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
