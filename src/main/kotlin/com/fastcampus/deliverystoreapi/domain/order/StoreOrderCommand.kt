package com.fastcampus.deliverystoreapi.domain.order

enum class StoreOrderCommand(val description: String) {
    ACCEPT("주문 수락"),
    REJECT("주문 거절"),
    CANCEL("주문 취소"),
    COMPLETE("조리 완료"),
}