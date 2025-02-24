package com.hansarangdelivery.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(title = "Hansarang Delivery API", version = "1.0", description = "API Documentation")
)
@Configuration
public class SwaggerConfig {

}
