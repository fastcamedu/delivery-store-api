package com.fastcampus.deliverystoreapi.repository.order

import com.fastcampus.deliverystoreapi.domain.order.StoreOrderStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.*

@Repository
interface StoreOrderRequestRepository: JpaRepository<StoreOrderRequest, Long> {

    @Query(
        value = """
            SELECT s
            FROM StoreOrderRequest as s
            WHERE s.storeId = :storeId
            AND s.storeOrderStatus IN :storeOrderStatuses
            AND s.createdAt BETWEEN :queryBaseDate AND :plusOneDayQueryBaseDate
        """
    )
    fun findAllBy(
        @Param("storeId") storeId: Long,
        @Param("storeOrderStatuses") storeOrderStatuses: List<StoreOrderStatus>,
        @Param("queryBaseDate") queryBaseDate: OffsetDateTime,
        @Param("plusOneDayQueryBaseDate") plusOneDayQueryBaseDate: OffsetDateTime,
        ): List<StoreOrderRequest>
    fun findByStoreIdAndOrderId(storeId: Long, orderId: Long): Optional<StoreOrderRequest>
    fun existsByOrderId(orderId: Long): Boolean
}

