package com.drinkeg.drinkeg.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "드링키지 API 명세서",
                description = "드링키지 API 명세서 페이지입니다",
                version = "v1"),
        servers = {@Server(url = "/", description = "Default Server URL")})
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {
    private final String SCHEME_NAME = "JWT Authentication";
    private final String COOKIE_NAME = "accessToken";

    /*@Bean
    public GroupedOpenApi chatOpenApi() {

        // "/v1/**" 경로에 매칭되는 API를 그룹화하여 문서화한다.
        String[] paths = {"/v1/**"};

        return GroupedOpenApi.builder()
                .group("드링키지 API v1")  // 그룹 이름을 설정한다.
                .pathsToMatch(paths)     // 그룹에 속하는 경로 패턴을 지정한다.
                .build();
    }*/

    @Bean
    public OpenAPI openAPI() {
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(SCHEME_NAME);
        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(new Components().addSecuritySchemes(SCHEME_NAME, createSecurityScheme()));
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name(COOKIE_NAME)
                .in(SecurityScheme.In.COOKIE)
                .type(SecurityScheme.Type.APIKEY);
    }
}