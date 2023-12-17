package com.fastcampus.deliverystoreapi.controller.order.dto

import com.fastcampus.deliverystoreapi.domain.order.StoreOrderStatus

data class StoreOrderCommandResponse(
    val orderId: Long,
    val storeId: Long,
    val changedStoreOrderStatus: StoreOrderStatus
)
