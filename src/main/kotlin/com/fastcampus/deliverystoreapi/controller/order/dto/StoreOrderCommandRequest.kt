package com.fastcampus.deliverystoreapi.controller.order.dto

import com.fastcampus.deliverystoreapi.domain.order.StoreOrderCommand

data class StoreOrderCommandRequest(
    val orderId: Long,
    val storeId: Long,
    val cookingMinutes: Int = NA,
    val storeOrderCommand: StoreOrderCommand = StoreOrderCommand.ACCEPT
) {
    companion object {
        private const val NA = -1
    }
}
