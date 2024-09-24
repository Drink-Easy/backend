package com.drinkeg.drinkeg.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.configuration.SpringDocSecurityConfiguration;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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

    // 로그아웃 엔드포인트 수동 추가
    PathItem logoutPath = new PathItem()
            .post(new io.swagger.v3.oas.models.Operation()
                    .summary("로그아웃")
                    .description("쿠키 및 Redis에 저장된 Refresh Token을 삭제하여 로그아웃을 진행합니다.")
                    .tags(List.of("logout-endpoint"))  // 태그 추가
                    .responses(new ApiResponses().addApiResponse("200",
                                    new ApiResponse().description("로그아웃 성공"))
                            .addApiResponse("401",
                                    new ApiResponse().description("인증 실패")))
            );

    /*@Bean
    public OpenApiCustomizer customLoginTagOpenApiCustomizer() {
        return openAPI -> {
            // 기존에 정의된 /login 경로에 접근
            Paths paths = openAPI.getPaths();
            PathItem loginPathItem = paths.get("/login");

            if (loginPathItem != null) {
                Operation loginPostOperation = loginPathItem.getPost();
                if (loginPostOperation != null) {
                    // 기존 태그를 "Authorization"으로 변경
                    loginPostOperation.setTags(List.of("Authorization"));
                }
            }
        };
    }*/

    @Bean
    public OpenAPI openAPI() {
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(SCHEME_NAME);
        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(new Components().addSecuritySchemes(SCHEME_NAME, createSecurityScheme()))
                .paths(new Paths()
                        .addPathItem("/logout", logoutPath)); // 로그아웃 엔드포인트 추가
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name(COOKIE_NAME)
                .in(SecurityScheme.In.COOKIE)
                .type(SecurityScheme.Type.APIKEY);
    }
}