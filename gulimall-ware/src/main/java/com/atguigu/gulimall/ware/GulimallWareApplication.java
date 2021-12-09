package com.atguigu.gulimall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 库存系统
 * wms_ware_info :仓库表
 * wms_ware_sku :每个仓库，几个商品，库存锁定件数
 * wms_ware_order_task :
 * wms_ware_order_task_detail :
 * @author lzx
 */
@EnableTransactionManagement
@MapperScan("com.atguigu.gulimall.ware.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallWareApplication {

  public static void main(String[] args) {
    SpringApplication.run(GulimallWareApplication.class, args);
  }
}
