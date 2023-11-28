package com.twohoseon.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : RestClientConfig
 * @date : 11/15/23 5:45 PM
 * @modifyed : $
 **/
@Configuration
public class RestClientConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
