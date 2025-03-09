package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// 업로드된 파일을 정적 리소스로 제공
@Configuration
public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/uploads/**") // 브라우저에서 /uploads/파일명 요청을 처리하도록 설정
//                .addResourceLocations("file:/home/ubuntu/Tests/uploads/"); // 실제 파일이 저장된 디렉토리를 매핑
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**") // 웹에서 /uploads/로 접근 가능
                .addResourceLocations("file:C:/Users/skcy1/OneDrive/Desktop/uploads");  // 로컬 폴더와 매핑
    }

}
