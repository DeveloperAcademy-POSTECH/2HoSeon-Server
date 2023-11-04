package com.twohoseon.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : yongjukim
 * @version : 1.0.0
 * @package : twohoseon
 * @name : ResourceConfiguration
 * @date : 2023/11/02
 * @modifyed : $
 **/

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Value("file:${file.dir}")
    private String filePath;

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/images/posts/**")
                .addResourceLocations(filePath + "posts/")
                .setCachePeriod(20);

        registry.addResourceHandler("/images/profiles/**")
                .addResourceLocations(filePath + "profiles/")
                .setCachePeriod(20);

        registry.addResourceHandler("/images/reviews/**")
                .addResourceLocations(filePath + "reviews/")
                .setCachePeriod(20);
    }
}