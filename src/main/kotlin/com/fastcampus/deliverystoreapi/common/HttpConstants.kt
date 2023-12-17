package com.fastcampus.deliverystoreapi.common

/**
 * 상점 API에서 공통으로 사용하는 상수를 정의한 클래스
 */
class HttpConstants {
    companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
        const val COOKIE_NAME_ACCESS_TOKEN = "AccessToken"
        const val COOKIE_NAME_STORE_ID = "storeId"
    }
}