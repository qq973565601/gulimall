package com.atguigu.gulimall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lzx
 * data:2021.11.5
 */
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallThirdPartyApplication {

  public static void main(String[] args) {
    SpringApplication.run(GulimallThirdPartyApplication.class, args);
  }
}
