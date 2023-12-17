package com.fastcampus.deliverystoreapi.config

import com.fastcampus.deliverystoreapi.domain.store.StoreRole
import com.fastcampus.deliverystoreapi.security.CustomAccessDeniedHandler
import com.fastcampus.deliverystoreapi.security.CustomAuthenticationEntryPoint
import com.fastcampus.deliverystoreapi.security.JwtAuthenticationFilter
import com.fastcampus.deliverystoreapi.security.JwtExceptionFilter
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * 배달 상점 API의 보안 설정을 정의한 클래스
 */
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authenticationProvider: AuthenticationProvider,
    private val objectMapper: ObjectMapper,
) {
    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
        jwtExceptionFilter: JwtExceptionFilter,
    ): DefaultSecurityFilterChain {
        http
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/hello",
                        "/apis/auth",
                        "/apis/auth/refresh",
                        "/apis/login",
                        "/error",
                        "/apis/geocode/**"
                    )
                    .permitAll()
                    .requestMatchers("/apis/stores/**")
                    .hasRole(StoreRole.OWNER.name)
                    .anyRequest()
                    .fullyAuthenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .exceptionHandling { it.accessDeniedHandler(CustomAccessDeniedHandler()) }
            .exceptionHandling { it.authenticationEntryPoint(CustomAuthenticationEntryPoint(objectMapper)) }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().applyPermitDefaultValues()
        configuration.allowedHeaders = listOf("*")
        configuration.allowedOrigins = listOf("http://localhost:20000", "http://localhost:20001", "http://localhost:30000")
        configuration.allowedMethods = listOf("*")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}