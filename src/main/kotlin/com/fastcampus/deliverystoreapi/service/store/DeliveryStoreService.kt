package com.fastcampus.deliverystoreapi.service.store

import com.fastcampus.deliverystoreapi.repository.store.Store
import com.fastcampus.deliverystoreapi.repository.store.StoreRepository
import org.springframework.stereotype.Service
import java.util.*

/**
 * 사장님 정보를 조회하는 서비스
 */
@Service
class DeliveryStoreService(
    private val storeRepository: StoreRepository,
) {
    fun findByEmail(email: String): Optional<Store> {
        return storeRepository.findByEmail(email)
    }
}