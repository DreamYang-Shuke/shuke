package com.polyPool.core;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决跨域问题
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * 跨域：SpringBoot添加支持CORS跨域访问
     * 单独配置给ZUUL，否则返回的响应头中会有重复值
     * @param registry
     * @return
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //Access-Control-Allow-Origin 为允许哪些Origin发起跨域请求. 这里设置为”*”表示允许所有，通常设置为所有并不安全，最好指定一下。
                .allowedOrigins("*")
                //Access-Control-Allow-Methods 为允许请求的方法.
                .allowedMethods("*")
                //Access-Control-Max-Age 表明在多少秒内，不需要再发送预检验请求，可以缓存该结果
                .maxAge(168000)
                //Access-Control-Allow-Headers 表明它允许跨域请求包含content-type头，这里设置的x-requested-with ，表示ajax请求
                .allowedHeaders("Access-Control-Allow-Headers", "Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization", "token")
                //Access-Control-Allow-Credentials 是否可以将对请求的响应暴露给页面。返回true则可以，其他值均不可以。
                .allowCredentials(true);
    }
}
