package com.example.travelcamp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {

    @Value("${upload.path}") //указан в app.properties
    private String uploadPath;

    //подхватываем путь хранения картинок
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //Если на каком-то шаблоне обращаемся по адресу /img/любоеЗначение, то необходимо открыть этот путь
        // ("file:///" означает, что открывать будем локальную директорию (можно ещё открывать на каком-либо сервере, например)
        registry.addResourceHandler("/img/uploadPath/**").addResourceLocations("file:///"+uploadPath+"/");
    }
}
