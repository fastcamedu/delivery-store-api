package com.fastcampus.deliverystoreapi.domain.order

enum class OrderStatus(val description: String) {
    READY("대기중"),
    COOKING("조리 중"),
    DELIVERING("배달 중"),
    CANCEL("취소"),
    COMPLETE("배달 완료")
}