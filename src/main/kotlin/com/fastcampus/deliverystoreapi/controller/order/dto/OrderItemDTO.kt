package com.fastcampus.deliverystoreapi.controller.order.dto

import com.fastcampus.deliverystoreapi.external.order.dto.ExternalOrderItemDTO
import java.math.BigDecimal

class OrderItemDTO(
    val orderId: Long,
    val orderItemId: Long,
    val menuId: Long,
    val menuName: String,
    val menuPrice: BigDecimal,
    val menuQuantity: Int,
) {
    companion object {
        fun of(externalOrderItemDTO: ExternalOrderItemDTO): OrderItemDTO {
            return OrderItemDTO(
                orderId = externalOrderItemDTO.orderId,
                orderItemId = externalOrderItemDTO.orderItemId,
                menuId = externalOrderItemDTO.menuId,
                menuName = externalOrderItemDTO.menuName,
                menuPrice = externalOrderItemDTO.menuPrice,
                menuQuantity = externalOrderItemDTO.menuQuantity,
            )
        }
    }
}