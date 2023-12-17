package com.fastcampus.deliverystoreapi.controller.order

import com.fastcampus.deliverystoreapi.controller.order.dto.OrderDetailResponse
import com.fastcampus.deliverystoreapi.controller.order.dto.OrderQueryResponse
import com.fastcampus.deliverystoreapi.external.order.dto.RequestStoreOrderStatus
import com.fastcampus.deliverystoreapi.service.order.OrderQueryService
import com.fastcampus.deliverystoreapi.service.order.StoreOrderRequestQueryService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * 상점에 들어온 주문 정보를 조회하는 컨트롤러
 */
@Tag(name = "StoreOrderQueryController", description = "상점에 들어온 주문을 조회하는 API 목록")
@CrossOrigin("*")
@RestController
class StoreOrderQueryController(
    private val storeOrderRequestQueryService: StoreOrderRequestQueryService,
    private val orderQueryService: OrderQueryService,
) {
    companion object {
        private val logger = KotlinLogging.logger {  }
    }

    @Operation(
        summary = "주문 조회 API", description = "상점에 들어온 주문을 조회하는 API"
    )
    @GetMapping("/apis/stores/{storeId}/orders")
    fun list(
        @PathVariable storeId: Long,
        @RequestParam("requestStoreOrderStatus") requestStoreOrderStatus: RequestStoreOrderStatus,
        @RequestParam("queryBaseDate") queryBaseDate: String,
    ): ResponseEntity<OrderQueryResponse> {

        logger.info { ">>> 상점에서 들어온 주문 정보 조회: $storeId, $requestStoreOrderStatus, $queryBaseDate" }
        val deliveryOrders = storeOrderRequestQueryService.findAllBy(storeId, requestStoreOrderStatus, queryBaseDate)

        return ResponseEntity.ok(
            OrderQueryResponse(
                storeId = storeId,
                orders = deliveryOrders,
            )
        )
    }

    @Operation(
        summary = "주문의 상세 정보를 조회하는 API", description = "주문 상세 정보를 조회하는 API"
    )
    @GetMapping("/apis/stores/{storeId}/orders/{orderId}")
    fun detail(
        @PathVariable("storeId") storeId: Long,
        @PathVariable("orderId") orderId: Long
    ): ResponseEntity<OrderDetailResponse> {
        val externalDeliveryOrderDTO = orderQueryService.detail(orderId)
        return ResponseEntity.ok(
            OrderDetailResponse.of(externalDeliveryOrderDTO)
        )
    }
}
