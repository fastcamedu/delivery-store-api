package com.fastcampus.deliverystoreapi.consumer.payment

import java.math.BigDecimal

/**
 * 결제 서비스에서 상점 서비스로 전달하는 메시지를 정의한 클래스
 */
data class PaymentMessage(
    val storeId: Long,
    val orderId: Long,
    val orderShortenId: String,
    val orderUUID: String,
    val customerId: Long,
    val paymentId: Long,
    val orderAmount: BigDecimal,
    val discountAmount: BigDecimal,
    val deliveryFee: BigDecimal,
    val promotionFee: BigDecimal,
    val payAmount: BigDecimal,
)