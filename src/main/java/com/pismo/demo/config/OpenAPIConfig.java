package com.pismo.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {

        final String securitySchemeName = "bearerAuth";

        OpenAPI openApi = new OpenAPI();

        Contact contact = new Contact();
        contact.setEmail("hrsprmr@gmail.com");
        contact.setName("Harshadkumar Parmar");
        contact.setUrl("https://github.com/harsh2792");

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .name(securitySchemeName)
                .scheme("bearer")
                .bearerFormat("JWT");

        Info info = new Info()
                .title("Transaction Management API")
                .version("1.0")
                .contact(contact)
                .description("API for Transaction Management");
        openApi.setInfo(info);
        openApi.addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
        openApi.components(new Components().addSecuritySchemes(securitySchemeName, securityScheme));
        return openApi;
    }
}
