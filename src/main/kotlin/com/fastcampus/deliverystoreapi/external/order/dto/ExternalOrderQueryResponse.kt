package com.fastcampus.deliverystoreapi.external.order.dto

class ExternalOrderQueryResponse(
    val storeId: Long,
    val orders: List<ExternalDeliveryOrderDTO>
)