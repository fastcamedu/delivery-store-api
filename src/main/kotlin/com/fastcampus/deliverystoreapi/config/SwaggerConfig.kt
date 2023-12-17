package com.fastcampus.deliverystoreapi.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(
    info = Info(
        title = "상점 배달 주문 서비스 API",
        description = "상점에 들어오는 음식 주문 서비스 처리하기 위한 API 목록",
        version = "0.1"
    )
)
@Configuration
class SwaggerConfig