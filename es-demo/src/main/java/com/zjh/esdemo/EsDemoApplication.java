package com.zjh.esdemo;

import io.searchbox.client.JestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EsDemoApplication {


    public static void main(String[] args) {
        SpringApplication.run(EsDemoApplication.class, args);
    }


}
