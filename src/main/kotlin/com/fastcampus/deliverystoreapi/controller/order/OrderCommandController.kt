package com.fastcampus.deliverystoreapi.controller.order

import com.fastcampus.deliverystoreapi.controller.order.dto.StoreOrderCommandRequest
import com.fastcampus.deliverystoreapi.controller.order.dto.StoreOrderCommandResponse
import com.fastcampus.deliverystoreapi.domain.order.StoreOrderCommand
import com.fastcampus.deliverystoreapi.service.order.OrderCommandService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * 주문 관련 명령을 처리하는 컨트롤러
 */
@Tag(name = "OrderCommandController", description = "상점에서 주문을 처리하는 API 목록")
@CrossOrigin("*")
@RestController
class OrderCommandController(
    private val orderCommandService: OrderCommandService
) {
    companion object {
        private val logger = KotlinLogging.logger {  }
    }

    @Operation(
        summary = "상점의 주문 처리 API", description = "상점에 들어온 주문을 처리하는 API"
    )
    @PutMapping("/apis/stores/orders/status")
    fun handle(
        @RequestBody request: StoreOrderCommandRequest
    ): ResponseEntity<StoreOrderCommandResponse> {
        logger.info { ">>> 상점의 주문 처리 요청: $request"}

        val changedStoreOrderRequest = when (request.storeOrderCommand) {
            StoreOrderCommand.ACCEPT -> this.orderCommandService.accept(
                orderId = request.orderId,
                storeId = request.storeId,
                cookingMinutes = request.cookingMinutes,
            )

            StoreOrderCommand.REJECT -> this.orderCommandService.reject(
                orderId = request.orderId,
                storeId = request.storeId
            )

            StoreOrderCommand.CANCEL -> this.orderCommandService.cancel(
                orderId = request.orderId,
                storeId = request.storeId
            )

            StoreOrderCommand.COMPLETE -> this.orderCommandService.complete(
                orderId = request.orderId,
                storeId = request.storeId
            )
        }
        return ResponseEntity.ok(
            StoreOrderCommandResponse(
                orderId = changedStoreOrderRequest.orderId,
                storeId = changedStoreOrderRequest.storeId,
                changedStoreOrderStatus = changedStoreOrderRequest.storeOrderStatus
            )
        )
    }
}

