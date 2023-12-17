package com.fastcampus.deliverystoreapi.external.order.dto

import java.math.BigDecimal

class ExternalOrderItemDTO(
    val orderId: Long,
    val orderItemId: Long,
    val menuId: Long,
    val menuName: String,
    val menuPrice: BigDecimal,
    val menuQuantity: Int,
)