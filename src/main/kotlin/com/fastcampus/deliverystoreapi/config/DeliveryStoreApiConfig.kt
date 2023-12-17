package com.fastcampus.deliverystoreapi.config

import com.fastcampus.deliverystoreapi.repository.store.StoreRepository
import com.fastcampus.deliverystoreapi.security.JwtProperties
import com.fastcampus.deliverystoreapi.service.store.DeliveryStoreDetailService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * 배달 상점 API의 설정을 정의한 클래스
 */
@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class DeliveryStoreApiConfig {

    @Bean
    fun userDetailsService(
        storeRepository: StoreRepository,
    ): UserDetailsService = DeliveryStoreDetailService(storeRepository)

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(
        storeRepository: StoreRepository,
    ): AuthenticationProvider =
        DaoAuthenticationProvider()
            .also {
                it.setUserDetailsService(userDetailsService(storeRepository))
                it.setPasswordEncoder(passwordEncoder())
            }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager
}