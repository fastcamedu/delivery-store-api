package com.fastcampus.deliverystoreapi.service.order

import com.fastcampus.deliverystoreapi.consumer.payment.PaymentMessage
import com.fastcampus.deliverystoreapi.controller.order.dto.DeliveryOrderDTO
import com.fastcampus.deliverystoreapi.domain.order.StoreOrderStatus
import com.fastcampus.deliverystoreapi.external.order.OrderAdapter
import com.fastcampus.deliverystoreapi.external.order.dto.RequestStoreOrderStatus
import com.fastcampus.deliverystoreapi.repository.order.StoreOrderRequest
import com.fastcampus.deliverystoreapi.repository.order.StoreOrderRequestRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.OffsetDateTime

/**
 * 상점에 들어온 주문 정보를 조회하는 서비스
 */
@Service
class StoreOrderRequestQueryService(
    private val storeOrderRequestRepository: StoreOrderRequestRepository,
    private val orderAdapter: OrderAdapter,
) {

    @Value("\${server.role-name")
    private lateinit var roleName: String

    companion object {
        private val logger = KotlinLogging.logger {  }
        private const val ONE_DAY = 1L
    }

    fun findAllBy(storeId: Long, requestStoreOrderStatus: RequestStoreOrderStatus, queryBaseDate: String): List<DeliveryOrderDTO> {

        val storeOrderStatuses = convertToStoreOrderStatuses(requestStoreOrderStatus)
        val startQueryBaseDate = OffsetDateTime.parse(queryBaseDate + "T00:00:00Z")
        val endQueryBaseDate = OffsetDateTime.parse(queryBaseDate + "T00:00:00Z").plusDays(ONE_DAY)
        val deliveryOrders = storeOrderRequestRepository.findAllBy(
            storeId = storeId, storeOrderStatuses = storeOrderStatuses, queryBaseDate = startQueryBaseDate, plusOneDayQueryBaseDate = endQueryBaseDate
        )
        val orderIds = deliveryOrders.map { it.orderId }

        val externalDeliveryOrderDTOS = orderAdapter.findAllByOrderIds(storeId, orderIds).orders

        val externalDeliveryOrderMap = externalDeliveryOrderDTOS.associateBy { it.orderId }
        val deliveryOrderDTOS = deliveryOrders.map { DeliveryOrderDTO.of(it) }

        deliveryOrderDTOS
            .filter { externalDeliveryOrderMap.containsKey(it.orderId) }
            .forEach {
                it.orderItems = externalDeliveryOrderMap[it.orderId]?.orderItems ?: emptyList()
                it.totalAmount = externalDeliveryOrderMap[it.orderId]?.totalAmount ?: BigDecimal.ZERO
            }

        return deliveryOrderDTOS
    }

    private fun convertToStoreOrderStatuses(requestStoreOrderStatus: RequestStoreOrderStatus): List<StoreOrderStatus> {
        return when (requestStoreOrderStatus) {
            RequestStoreOrderStatus.READY -> return listOf(StoreOrderStatus.READY)
            RequestStoreOrderStatus.COOKING -> return listOf(StoreOrderStatus.COOKING)
            RequestStoreOrderStatus.DELIVERING -> return listOf(StoreOrderStatus.DELIVERING)
            RequestStoreOrderStatus.COMPLETE -> return listOf(StoreOrderStatus.COMPLETE)
            RequestStoreOrderStatus.ALL -> return StoreOrderStatus.values().toList()
            RequestStoreOrderStatus.CANCEL -> return listOf(StoreOrderStatus.CANCEL)
        }
    }

    fun create(paymentMessage: PaymentMessage): StoreOrderRequest {
        val storeOrderRequest = StoreOrderRequest.of(paymentMessage, roleName)
        return this.storeOrderRequestRepository.save(storeOrderRequest)
    }

    fun existByOrderId(orderId: Long): Boolean {
        return storeOrderRequestRepository.existsByOrderId(orderId)
    }
}