package com.fastcampus.deliverystoreapi.service.auth


import com.fastcampus.deliverypartnerapi.controller.auth.dto.AuthenticationResponse
import com.fastcampus.deliverystoreapi.controller.auth.dto.AuthenticationRequest
import com.fastcampus.deliverystoreapi.repository.token.RefreshToken
import com.fastcampus.deliverystoreapi.repository.token.RefreshTokenRepository
import com.fastcampus.deliverystoreapi.security.JwtProperties
import com.fastcampus.deliverystoreapi.service.store.DeliveryStoreService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

/**
 * 인증 처리를 담당하는 서비스
 */
@Service
class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val deliveryStoreService: DeliveryStoreService,
    private val userDetailsService: UserDetailsService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository,
) {

    fun authentication(authenticationRequest: AuthenticationRequest): AuthenticationResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authenticationRequest.email,
                authenticationRequest.password
            )
        )
        val storeOptional = deliveryStoreService.findByEmail(authenticationRequest.email)
        if (storeOptional.isEmpty) {
            error("사장님 정보가 없습니다.")
        }

        val store = storeOptional.get()
        val accessToken = createAccessToken(store.email)
        val refreshToken = createRefreshToken(store.email)

        val refreshTokenOptional = refreshTokenRepository.findByEmail(store.email)
        if (refreshTokenOptional.isEmpty) {
            refreshTokenRepository.save(RefreshToken(email = store.email, refreshToken = refreshToken))
        } else {
            val savedRefreshToken = refreshTokenOptional.get()
            savedRefreshToken.refreshToken = refreshToken
            refreshTokenRepository.save(savedRefreshToken)
        }

        return AuthenticationResponse(
            storeId = store.storeId,
            email = authenticationRequest.email,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    fun refreshAccessToken(refreshToken: String): String? {
        val extractedEmail = tokenService.extractEmail(refreshToken)

        return extractedEmail?.let { email ->
            val currentUserDetails = userDetailsService.loadUserByUsername(email)
            val refreshTokenOptional = refreshTokenRepository.findByEmail(email)
            if (!tokenService.isExpired(refreshToken) && refreshTokenOptional.get().email == currentUserDetails.username) {
                createAccessToken(currentUserDetails.username)
            } else {
                null
            }
        }
    }

    private fun createAccessToken(email: String) = tokenService.generate(
        email = email,
        expirationDate = getAccessTokenExpiration()
    )

    private fun createRefreshToken(email: String) = tokenService.generate(
        email = email,
        expirationDate = getRefreshTokenExpiration()
    )

    private fun getAccessTokenExpiration(): Date =
        Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)

    private fun getRefreshTokenExpiration(): Date =
        Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration)
}