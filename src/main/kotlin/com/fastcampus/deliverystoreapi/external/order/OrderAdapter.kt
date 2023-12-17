package com.fastcampus.deliverystoreapi.external.order

import com.fastcampus.deliverystoreapi.exception.NotFoundOrderException
import com.fastcampus.deliverystoreapi.external.order.dto.ExternalDeliveryOrderDTO
import com.fastcampus.deliverystoreapi.external.order.dto.ExternalOrderQueryResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

/**
 * 외부 주문 API 서버와 통신하는 어댑터
 */
@Component
class OrderAdapter(
    private val restTemplate: RestTemplate,
) {
    companion object {
        private val logger = KotlinLogging.logger {  }
    }

    @Value("\${external.apis.sever.delivery-order-api.host}")
    private lateinit var deliveryOrderApi: String

    @Value("\${external.apis.sever.delivery-order-api.orders}")
    private lateinit var orderPath: String

    @Value("\${external.apis.sever.delivery-order-api.order-ids}")
    private lateinit var orderIdsPath: String

    @Value("\${external.apis.sever.delivery-order-api.orders}")
    private lateinit var orderDetailPath: String

    fun findAllByOrderIds(
        storeId: Long,
        orderIds: List<Long>
    ): ExternalOrderQueryResponse {

        val orderApiFullPath = "$deliveryOrderApi$orderIdsPath?storeId=$storeId&orderIds=${orderIds.joinToString(",")}"
        logger.info { ">>> 주문 목록 요청: $orderApiFullPath" }

        val responseEntity =
            restTemplate.getForEntity(orderApiFullPath, ExternalOrderQueryResponse::class.java)

        logger.info { ">>> 주문 목록 응답: ${responseEntity.body?.orders?.size}" }

        return responseEntity.body ?: throw NotFoundOrderException("주문 정보를 찾을 수 없습니다.")
    }

    fun findAllBy(
        storeId: Long,
        queryBaseDate: String,
    ): ExternalOrderQueryResponse {

        val orderApiFullPath = "$deliveryOrderApi$orderPath?storeId=$storeId&&queryBaseDate=$queryBaseDate"
        logger.info { ">>> 주문 목록 요청: $orderApiFullPath" }

        val responseEntity =
            restTemplate.getForEntity(orderApiFullPath, ExternalOrderQueryResponse::class.java)

        logger.info { ">>> 주문 목록 응답: ${responseEntity.body}" }

        return responseEntity.body ?: throw NotFoundOrderException("체크아웃 정보를 찾을 수 없습니다.")
    }

    fun detail(orderId: Long): ExternalDeliveryOrderDTO {

        val orderDetailFullPath = "$deliveryOrderApi$orderDetailPath/$orderId"
        logger.info { ">>> 주문 상세 정보 요청: $orderDetailFullPath" }

        val responseEntity =
            restTemplate.getForEntity(orderDetailFullPath, ExternalDeliveryOrderDTO::class.java)

        return responseEntity.body ?: throw NotFoundOrderException("체크아웃 정보를 찾을 수 없습니다.")
    }
}