package com.fastcampus.deliverystoreapi.service.store

import com.fastcampus.deliverystoreapi.domain.store.StoreRole
import com.fastcampus.deliverystoreapi.repository.store.StoreRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

/**
 * 사장님 정보를 조회하는 서비스 (인증 관련)
 */
class DeliveryStoreDetailService(
    private val storeRepository: StoreRepository,
): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val store = storeRepository.findByEmail(username!!).orElseThrow { error("사용자 정보가 없습니다.") }
        return User.builder()
            .username(username)
            .password(store.password)
            .roles(StoreRole.OWNER.name)
            .build()
    }
}