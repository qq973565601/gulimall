package com.atguigu.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 实现功能
 * 1、
 *
 *
 * 2、逻辑删除
 *      1）、配置全局逻辑删除规则
 *      2）、日志逻辑删除的组件的 Bean
 *      3）、给 Bean 加上逻辑删除注解 @TableLogic
 *
 * 3、JSR303校验
 *      1）、给 Bean 添加校验注解：javax.validation.constraints ，并自定义 message
 *      2）、开启校验功能，在需要校验的 Bean 上添加注解 @Valid
 *          效果：校验错误以后会有默认的响应
 *      3）、校验的 Bean 后加上 BindingResult，可以获得校验的结果
 *      4）、分组校验（多场景的复杂校验）
 *          (1)、给校验注解标注什么情况需要进行校验；
 *          (2)、@Validated({AddGroup.class})
 *          (3)默认没有指定分组的校验注解@NotBlank，在分组校验情况 Validated({AddGroup.class})下不生效，只会在@Validated生效；
 *      5）、自定义校验
 *          (1)、编写一个自定义的校验注解
 *          (2)、编写一个自定义的校验器 ConstraintValidator
 *          (3)、关联自定义的校验器和自定义的校验注解
 * 4、通一异常处理
 *      1）、使用 SpringMvc 提供的 @ControllerAdvice
 *      2）、使用 @ExceptionHandler 标注要处理的异常
 *      3）、使用枚举统一状态码
 */



/**
 * @author lzx
 */
@EnableFeignClients(basePackages = "com.atguigu.gulimall.product.feign")
@EnableDiscoveryClient
@MapperScan("com.atguigu.gulimall.product.dao")
@SpringBootApplication
public class GulimallProductApplication {

  public static void main(String[] args) {
    SpringApplication.run(GulimallProductApplication.class, args);
  }
}
