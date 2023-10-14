package com.study.CSStudy.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title="Swagger Sample API",
                description = "Swagger Docs example Code",
                version = "Ver 1"
        )
)
public class SwaggerConfig {
    private static final  String BEARER_TOKEN = "BEARER";

//    @Bean
//    public OpenAPI openAPI() {
//        String jwtSchemeName = JwtTokenProvider.AUTHORIZATION_HEADER;
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
//        Components components = new Components()
//                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
//                        .name(jwtSchemeName)
//                        .type(SecurityScheme.Type.HTTP)
//                        .scheme(BEARER_TOKEN_PREFIX)
//                        .bearerFormat(JwtTokenProvider.TYPE));
//
//        // Swagger UI 접속 후, 딱 한 번만 accessToken을 입력해주면 모든 API에 토큰 인증 작업이 적용됩니다.
//        return new OpenAPI()
//                .addSecurityItem(securityRequirement)
//                .components(components);
//    }
}
