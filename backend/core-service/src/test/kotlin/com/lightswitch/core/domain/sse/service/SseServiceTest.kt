package com.lightswitch.core.domain.sse.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional


@SpringBootTest
class SseServiceTest(
    @Autowired
    private val sseService: SseService
) {
    @Test
    fun `SDK Key로 User Key 생성`() {
        val sdkKey = "0801d3c5e29b4fc3bbfe9023716891b8"

        val userKey = sseService.createUserKey(sdkKey).userKey

        println(userKey)
        assertThat(userKey).isNotBlank()
    }
}