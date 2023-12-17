package com.fastcampus.deliverystoreapi.security

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * JWT 설정 정보
 */
@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val key: String,
    val accessTokenExpiration: Long,
    val refreshTokenExpiration: Long,
)