package com.fastcampus.deliverystoreapi.controller.order.dto

import com.fastcampus.deliverystoreapi.domain.order.StoreOrderStatus
import com.fastcampus.deliverystoreapi.repository.order.StoreOrderRequest
import java.math.BigDecimal

data class DeliveryOrderDTO(
    val orderId: Long,
    val orderShortenId: String,
    val storeOrderStatus: StoreOrderStatus,
    val storeId: Long,
    val customerId: Long,
    var totalAmount: BigDecimal? = null,
    var orderItems: List<OrderItemDTO> = emptyList()
) {
    companion object {
        fun of(storeOrderRequest: StoreOrderRequest): DeliveryOrderDTO {
            return DeliveryOrderDTO(
                orderId = storeOrderRequest.orderId,
                orderShortenId = storeOrderRequest.orderShortenId,
                storeOrderStatus = storeOrderRequest.storeOrderStatus,
                storeId = storeOrderRequest.storeId,
                customerId = storeOrderRequest.customerId,
            )
        }
    }
}