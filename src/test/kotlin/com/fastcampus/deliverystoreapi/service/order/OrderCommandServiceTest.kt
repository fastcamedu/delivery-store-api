package com.fastcampus.deliverystoreapi.service.order

import com.fastcampus.deliverystoreapi.domain.order.StoreOrderStatus
import com.fastcampus.deliverystoreapi.repository.order.StoreOrderRequest
import com.fastcampus.deliverystoreapi.repository.order.StoreOrderRequestRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class OrderCommandServiceTest {

    @Mock
    private lateinit var storeOrderRequestRepository: StoreOrderRequestRepository

    @InjectMocks
    private lateinit var orderCommandService: OrderCommandService

    @Test
    fun `주문 대기인 경우만 주문 수락할 수 있다`() {
        // given
        val storeId = 1L
        val orderId = 2L
        val cookingMinutes = 10
        val storeOrderStatus = StoreOrderStatus.READY
        `when`(storeOrderRequestRepository.findByStoreIdAndOrderId(storeId, orderId)).thenReturn(
            createStoreOrderRequest(storeId = storeId, orderId = orderId, storeOrderStatus = storeOrderStatus)
        )

        // when
        assertDoesNotThrow {
            orderCommandService.accept(storeId = storeId, orderId = orderId, cookingMinutes = cookingMinutes)
        }
    }

    @ParameterizedTest(name = "주문이 {0}인 경우는 조리 완료가 가능하다")
    @EnumSource(mode = EnumSource.Mode.INCLUDE, names = ["READY", "ACCEPT"])
    fun `조리 완료는 "주문 대기" 또는 "주문 수락"인 경우만 가능하다`(storeOrderStatus: StoreOrderStatus) {
        // given
        val storeId = 1L
        val orderId = 2L
        `when`(storeOrderRequestRepository.findByStoreIdAndOrderId(storeId, orderId)).thenReturn(
            createStoreOrderRequest(storeId = storeId, orderId = orderId, storeOrderStatus = storeOrderStatus)
        )

        // when
        assertDoesNotThrow {
            orderCommandService.complete(storeId = storeId, orderId = orderId)
        }
    }

    private fun createStoreOrderRequest(storeId: Long, orderId: Long, storeOrderStatus: StoreOrderStatus): Optional<StoreOrderRequest> {
        val orderUUID = UUID.randomUUID().toString()
        val orderShortenId = orderUUID.substring(0..7)
        return Optional.of(
            StoreOrderRequest(
                storeOrderRequestId = 1L,
                storeId = storeId,
                orderId = orderId,
                storeOrderStatus = storeOrderStatus,
                customerId = 4L,
                orderUUID = orderUUID,
                orderShortenId = orderShortenId,
            )
        )
    }
}