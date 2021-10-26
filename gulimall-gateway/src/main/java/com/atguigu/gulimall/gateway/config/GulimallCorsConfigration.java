package com.atguigu.gulimall.gateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 跨域处理(过滤)
 * @author lzx
 */
@Configuration
public class GulimallCorsConfigration {

    @Bean
    public CorsWebFilter corsWebFilter(){

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 1、配置跨域
        // 允许请求头
        corsConfiguration.addAllowedHeader("*");
        // 允许请求方法
        corsConfiguration.addAllowedMethod("*");
        // 允许请求来源
        corsConfiguration.addAllowedOrigin("*");
        // 是否允许携带cookie
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**",corsConfiguration);
        return new CorsWebFilter(source);
    }
}
