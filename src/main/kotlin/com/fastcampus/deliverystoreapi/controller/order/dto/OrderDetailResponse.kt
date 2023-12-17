package com.fastcampus.deliverystoreapi.controller.order.dto

import com.fastcampus.deliverystoreapi.external.order.dto.ExternalDeliveryOrderDTO

data class OrderDetailResponse(
    val orderId: Long,
    val orderShortenId: String,
    val storeId: Long,
    val customerId: Long,
    val orderItems: List<OrderItemDTO>
) {
    companion object {
        fun of(externalOrderDTO: ExternalDeliveryOrderDTO): OrderDetailResponse {
            return OrderDetailResponse(
                orderId = externalOrderDTO.orderId,
                orderShortenId = externalOrderDTO.orderShortenId,
                storeId = externalOrderDTO.storeId,
                customerId = externalOrderDTO.customerId,
                orderItems = externalOrderDTO.orderItems,
            )
        }
    }
}
