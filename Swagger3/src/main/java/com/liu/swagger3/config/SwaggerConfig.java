package com.liu.swagger3.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author liu
 */
@Configuration

public class SwaggerConfig {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                /*
                    any() // 扫描所有，项目中的所有接口都会被扫描到
                    none() // 不扫描接口
                    // 通过方法上的注解扫描，
                    如withMethodAnnotation(GetMapping.class)只扫描get请求
                    withMethodAnnotation(final Class<? extends Annotation> annotation)
                    // 通过类上的注解扫描，
                    如.withClassAnnotation(Controller.class)只扫描有controller注解的类中的接口
                    withClassAnnotation(final Class<? extends Annotation> annotation)
                    basePackage(final String basePackage) // 根据包路径扫描接口
                 */
                .apis(RequestHandlerSelectors.basePackage("com.liu.swagger3.controller"))
                /*
                    进行请求url的过滤
                    any() // 任何请求都扫描
                    none() // 任何请求都不扫描
                    regex(final String pathRegex) // 通过正则表达式控制
                    ant(final String antPattern) // 通过ant()控制
                    例如：ant("/user")那么只扫描/user开头的请求
                 */
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Swagger3的api文档")
                .description("如有意外请联系工程师")
                .contact(new Contact("liushuai","https://www.baidu.com","1906664893@qq.com"))
                .version("2.0")
                .build();
    }
}
