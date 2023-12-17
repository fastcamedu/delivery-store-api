package com.fastcampus.deliverypartnerapi.controller.auth.dto

data class AuthenticationResponse(
    val storeId: Long,
    val email: String,
    val accessToken: String,
    val refreshToken: String,
)