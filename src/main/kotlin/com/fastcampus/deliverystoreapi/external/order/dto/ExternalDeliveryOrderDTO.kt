package com.fastcampus.deliverystoreapi.external.order.dto

import com.fastcampus.deliverystoreapi.controller.order.dto.OrderItemDTO
import com.fastcampus.deliverystoreapi.domain.order.OrderStatus
import java.math.BigDecimal

class ExternalDeliveryOrderDTO(
    val orderId: Long,
    val orderShortenId: String,
    val orderStatus: OrderStatus,
    val storeId: Long,
    val customerId: Long,
    val totalAmount: BigDecimal,
    var orderItems: List<OrderItemDTO> = emptyList()
)