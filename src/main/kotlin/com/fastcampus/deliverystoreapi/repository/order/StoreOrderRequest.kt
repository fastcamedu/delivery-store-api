package com.fastcampus.deliverystoreapi.repository.order

import com.fastcampus.deliverystoreapi.consumer.payment.PaymentMessage
import com.fastcampus.deliverystoreapi.domain.order.StoreOrderStatus
import com.fastcampus.deliverystoreapi.repository.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "store_order_requests", schema = "delivery_store")
data class StoreOrderRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_order_request_id")
    val storeOrderRequestId: Long = 0L,

    @Column(name = "order_id")
    val orderId: Long,

    @Column(name = "order_shorten_id")
    val orderShortenId: String,

    @Column(name = "order_uuid")
    val orderUUID: String,

    @Column(name = "store_id")
    val storeId: Long,

    @Column(name = "customer_id")
    val customerId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "store_order_status")
    var storeOrderStatus: StoreOrderStatus,

    @Column(name = "cooking_minutes")
    var cookingMinutes: Int = DEFAULT_COOKING_MINUTES,

): BaseEntity() {
    companion object {

        private const val DEFAULT_COOKING_MINUTES = 10

        fun of(payment: PaymentMessage, roleName: String): StoreOrderRequest {
            val storeOrderRequest = StoreOrderRequest(
                orderId = payment.orderId,
                orderShortenId = payment.orderShortenId,
                orderUUID = payment.orderUUID,
                storeId = payment.storeId,
                customerId = payment.customerId,
                storeOrderStatus = StoreOrderStatus.READY,
            )
            storeOrderRequest.createdBy = roleName
            storeOrderRequest.updatedBy = roleName
            return storeOrderRequest
        }
    }
}
