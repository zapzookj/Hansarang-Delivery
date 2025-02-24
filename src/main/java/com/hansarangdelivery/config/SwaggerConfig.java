package com.hansarangdelivery.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "Authorization";
        return new OpenAPI()
            .info(new Info().title("Hansarang Delivery API")
                .description("Hansarang Delivery API swagger 입니다.")
                .version("1.0"))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
            )
            .paths(customPaths());

    }

    private Paths customPaths() {
        return new Paths()
            .addPathItem("/api/users/login", createLoginPath());
    }

    private PathItem createLoginPath() {
        return new PathItem()
            .post(new Operation()
                .tags(List.of("Authentication"))
                .summary("User Login")
                .description("Authenticate a user and return a JWT token")
                .requestBody(new RequestBody()
                    .content(new Content()
                        .addMediaType("application/json",
                            new MediaType()
                                .schema(new Schema<>()
                                    .type("object")
                                    .addProperties("username", new Schema<>().type("string"))
                                    .addProperties("password", new Schema<>().type("string"))
                                )
                                .example(
                                    "{\"username\": \"user@example.com\", \"password\": \"password123\"}")
                        )
                    )
                )
                .responses(new ApiResponses()
                    .addApiResponse("200", new ApiResponse()
                        .description("Successful authentication")
                        .content(new Content()
                            .addMediaType("application/json",
                                new MediaType()
                                    .schema(new Schema<>()
                                        .type("object")
                                        .addProperties("token", new Schema<>().type("string"))
                                    )
                            )
                        )
                    )
                    .addApiResponse("401", new ApiResponse().description("Authentication failed"))
                )
            );
    }
}
