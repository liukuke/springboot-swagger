package com.liu.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2 // 开启Swagger2的自动配置
public class SwaggerConfig {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()// 通过.select()方法，去配置扫描接口,RequestHandlerSelectors配置如何扫描接口
                .apis(RequestHandlerSelectors.basePackage("com.liu.swagger.controller"))
                .paths(PathSelectors.any())// 配置如何通过path过滤,即这里扫描任何请求
                .build()
                ;
    }
    private ApiInfo apiInfo(){
        /*return new ApiInfo(
                "swagger2文档学习",// title 标题
                "学习如何配置swagger",//description 描述
                    "v1.0",// version 版本
                "http://terms.service.url/组织链接",// temsOfServiceUrl 组织连接
                new Contact("liushuai","http://localhost:8080/liushuai","liushuai@qq.com"),// contact 联系人信息
                "Apach 2.0 许可",// license 许可
                "许可链接",// licenseUrl 许可连接
                new ArrayList<>()// 扩展
        );*/
        // swagger基本信息
        return new ApiInfoBuilder()
                .title("swaggerDemo")
                .description("练习swagger")
                .version("1.0")
                .contact(new Contact("liushuai","http://localhost:8080/liushuai","liushuai@qq.com"))
                .build();
    }
}
