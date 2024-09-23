package com.mymes.equip.tc.endpoints;
//package com.mymes.equip.tc.endpoints;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
///**
// * Docket: Swagger 설정의 핵심이 되는 Bean
// * useDefaultResponseMessages: Swagger 에서 제공해주는 기본 응답 코드 (200, 401, 403, 404). false 로 설정하면 기본 응답 코드를 노출하지 않음
// * apis: api 스펙이 작성되어 있는 패키지 (Controller) 를 지정
// * paths: apis 에 있는 API 중 특정 path 를 선택
// * apiInfo:Swagger UI 로 노출할 정보
// */
//@Configuration
//@EnableWebMvc
//@EnableSwagger2
//@EnableAsync
//public class SwaggerConfig implements WebMvcConfigurer {
//	
//    private static final String SERVICE_NAME="Tool Control Service";
//    private static final String API_VERSION="v1";
//    private static final String API_DESCRIPTION="MakeProject API TEST";
//    private static final String API_URL="http://localhost:8080/";
//
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.any()) // RequestMapping의 모든 URL LIST
//                .paths(PathSelectors.any()) // .any() -> ant(/api/**") /api/**인 URL만 표시
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder().title(SERVICE_NAME)
//                .version(API_VERSION)
//                .description(API_DESCRIPTION)
//                .termsOfServiceUrl(API_URL)
//                .licenseUrl("라이센스 표시할 url")
//                .build();
//    }
//    
//    // 아래 부분은 WebMvcConfigure 를 상속받아서 설정하는 Mehtod 
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//        // Static resources
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//    }
//}
