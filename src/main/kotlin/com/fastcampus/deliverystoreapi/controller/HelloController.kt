package com.fastcampus.deliverystoreapi.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * API 서버의 기본 경로를 처리하는 컨트롤러
 */
@RestController
class HelloController {

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello Delivery Store API"
    }
}