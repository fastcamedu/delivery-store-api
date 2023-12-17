package com.fastcampus.deliverystoreapi.consumer.payment

import com.fastcampus.deliverystoreapi.service.order.StoreOrderRequestQueryService
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.dao.DuplicateKeyException
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

/**
 * 결제 완료 메시지를 수신하는 컨슈머
 */
@Service
class DeliveryPaymentMessageConsumer(
    private val storeOrderRequestQueryService: StoreOrderRequestQueryService,
    private val objectMapper: ObjectMapper,
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @KafkaListener(
        groupId = "delivery-payment-group",
        topics = ["delivery-payment-complete"],
    )
    fun receiveMessage(message: String) {
        logger.info { "Received message: $message" }

        val payment = objectMapper.readValue(message, PaymentMessage::class.java)
        validateStoreOrderRequest(payment)

        // 상점 주문 요청 저장
        this.storeOrderRequestQueryService.create(payment)
    }

    private fun validateStoreOrderRequest(payment: PaymentMessage) {
        if (storeOrderRequestQueryService.existByOrderId(payment.orderId)) {
            throw DuplicateKeyException("이미 처리된 결제 정보입니다. $payment")
        }
    }
}