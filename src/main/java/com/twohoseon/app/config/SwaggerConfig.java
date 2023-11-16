package com.twohoseon.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


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
                .description("임시 API 아래 Authorize를 클릭하고 Bearer에 토큰을 입력하세요.")
                .termsOfService("http://swagger.io/terms/")
                .contact(new Contact().name("hyunwoopark").email("migusdn@gmail.com"))
                .license(new License().name("Apache License Version 2.0").url("http://www.apache.org/licenses/LICENSE-2.0"));

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");
        OpenAPI openAPI = new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(List.of(securityRequirement))
                .info(info);

        try {
            // Load the custom YAML file
            Yaml yaml = new Yaml();
            InputStream inputStream = new ClassPathResource("swagger.yaml").getInputStream();
            Map<String, Object> yamlMap = yaml.load(inputStream);

            // Get the paths from the custom YAML file
            Map<String, LinkedHashMap> yamlPaths = (Map<String, LinkedHashMap>) yamlMap.get("paths");

            // Get the paths from the auto-generated OpenAPI documentation
            Paths openAPIPaths = openAPI.getPaths();

            // If openAPIPaths is null, initialize it
            if (openAPIPaths == null) {
                openAPIPaths = new Paths();
                openAPI.setPaths(openAPIPaths);
            }

            // Merge the paths
            for (Map.Entry<String, LinkedHashMap> entry : yamlPaths.entrySet()) {
                String path = entry.getKey();
                LinkedHashMap yamlPathItemMap = entry.getValue();

                // Convert the LinkedHashMap to a PathItem
                ObjectMapper mapper = Json.mapper();
                PathItem yamlPathItem = mapper.convertValue(yamlPathItemMap, PathItem.class);

                // If the path already exists in the auto-generated documentation, update it
                if (openAPIPaths.containsKey(path)) {
                    PathItem openAPIPathItem = openAPIPaths.get(path);

                    // Merge the operations
                    openAPIPathItem.readOperations().forEach(operation -> {
                        // Get the responses from the auto-generated documentation
                        ApiResponses openAPIResponses = operation.getResponses();

                        // Get the responses from the custom YAML file
                        ApiResponses yamlResponses = yamlPathItem.readOperations().get(0).getResponses();

                        // Merge the responses
                        openAPIResponses.putAll(yamlResponses);
                    });
                } else {
                    // If the path does not exist in the auto-generated documentation, add it
                    openAPIPaths.addPathItem(path, yamlPathItem);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load custom YAML file", e);
        }

        return openAPI;
    
    }

}
