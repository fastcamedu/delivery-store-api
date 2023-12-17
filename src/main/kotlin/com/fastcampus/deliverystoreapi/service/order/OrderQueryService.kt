package com.fastcampus.deliverystoreapi.service.order

import com.fastcampus.deliverystoreapi.external.order.OrderAdapter
import com.fastcampus.deliverystoreapi.external.order.dto.ExternalDeliveryOrderDTO
import org.springframework.stereotype.Service

/**
 * 주문을 조회하는 서비스
 */
@Service
class OrderQueryService(
    private val orderAdapter: OrderAdapter
) {
    fun detail(orderId: Long): ExternalDeliveryOrderDTO {
        return orderAdapter.detail(orderId)
    }
}
