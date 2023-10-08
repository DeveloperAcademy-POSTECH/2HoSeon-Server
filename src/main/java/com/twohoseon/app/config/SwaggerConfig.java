package com.twohoseon.app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : SwaggerConfig
 * @date : 2023/10/09 2:29 AM
 * @modifyed : $
 **/

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String appVersion) {
        Info info = new Info().title("Demo API").version(appVersion)
                .description("임시 API")
                .termsOfService("http://swagger.io/terms/")
                .contact(new Contact().name("hyunwoopark").email("migusdn@gmail.com"))
                .license(new License().name("Apache License Version 2.0").url("http://www.apache.org/licenses/LICENSE-2.0"));
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
