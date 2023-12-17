package com.fastcampus.deliverystoreapi.controller.order.dto

data class OrderQueryResponse(
    val storeId: Long,
    val orders: List<DeliveryOrderDTO>
)