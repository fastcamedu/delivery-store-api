package com.fastcampus.deliverystoreapi.controller.message

import com.fastcampus.deliverystoreapi.intrastructure.kafka.KafkaProduceService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 카프카 메시지를 테스트하기 위해 직접 발행하는 컨트롤러
 */
@RestController
@RequestMapping
class KafkaController(
    private val kafkaProduceService: KafkaProduceService
) {
    companion object {
        private const val TOPIC = "delivery-payment-complete"
    }

    @PostMapping("/kafka/publish")
    fun sendMessage(@RequestBody message: String) {
        kafkaProduceService.sendMessage(TOPIC, message)
    }
}