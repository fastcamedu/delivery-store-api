package com.fastcampus.deliverystoreapi.service.order

import com.fastcampus.deliverystoreapi.domain.order.StoreOrderStatus.CANCEL
import com.fastcampus.deliverystoreapi.domain.order.StoreOrderStatus.COMPLETE
import com.fastcampus.deliverystoreapi.domain.order.StoreOrderStatus.COOKING
import com.fastcampus.deliverystoreapi.domain.order.StoreOrderStatus.DELIVERING
import com.fastcampus.deliverystoreapi.domain.order.StoreOrderStatus.READY
import com.fastcampus.deliverystoreapi.exception.NotFoundOrderException
import com.fastcampus.deliverystoreapi.repository.order.StoreOrderRequest
import com.fastcampus.deliverystoreapi.repository.order.StoreOrderRequestRepository
import org.springframework.stereotype.Service

/**
 * 주문을 처리하는 서비스
 */
@Service
class OrderCommandService(
    private val storeOrderRequestRepository: StoreOrderRequestRepository
) {
    fun accept(storeId: Long, orderId: Long, cookingMinutes: Int): StoreOrderRequest {
        val storeOrderRequest = findStoreOrderRequest(storeId, orderId)
        validateAcceptStatus(storeOrderRequest)

        storeOrderRequest.storeOrderStatus = COOKING
        storeOrderRequest.cookingMinutes = cookingMinutes

        return this.storeOrderRequestRepository.save(storeOrderRequest)
    }

    fun cancel(storeId: Long, orderId: Long): StoreOrderRequest {
        val storeOrderRequest = findStoreOrderRequest(storeId, orderId)
        storeOrderRequest.storeOrderStatus = CANCEL
        return this.storeOrderRequestRepository.save(storeOrderRequest)
    }

    fun reject(storeId: Long, orderId: Long): StoreOrderRequest {
        val storeOrderRequest = findStoreOrderRequest(storeId, orderId)
        storeOrderRequest.storeOrderStatus = CANCEL
        return this.storeOrderRequestRepository.save(storeOrderRequest)
    }

    fun complete(storeId: Long, orderId: Long): StoreOrderRequest {
        val storeOrderRequest = findStoreOrderRequest(storeId, orderId)
        validateCompleteStatus(storeOrderRequest)

        storeOrderRequest.storeOrderStatus = COMPLETE
        return this.storeOrderRequestRepository.save(storeOrderRequest)
    }

    private fun validateCompleteStatus(storeOrderRequest: StoreOrderRequest) {
        when (storeOrderRequest.storeOrderStatus) {
            READY, COOKING, DELIVERING -> return
            else -> {
                throw IllegalStateException("조리 완료는 '주문 대기'와 '주문 수락'인 경우만 가능합니다")
            }
        }
    }

    private fun validateAcceptStatus(storeOrderRequest: StoreOrderRequest) {
        when (storeOrderRequest.storeOrderStatus) {
            READY -> return
            else -> {
                throw IllegalStateException("주문 수락은 '주문 대기'일 때만 가능합니다. ${storeOrderRequest.storeOrderStatus}")
            }
        }
    }

    private fun findStoreOrderRequest(storeId: Long, orderId: Long): StoreOrderRequest {
        val storeOrderRequestOptional = storeOrderRequestRepository.findByStoreIdAndOrderId(storeId, orderId)
        if (storeOrderRequestOptional.isEmpty) {
            throw NotFoundOrderException(">>> 주문 정보가 없습니다. storeId: $storeId, orderId: $orderId")
        }
        return storeOrderRequestOptional.get()
    }
}