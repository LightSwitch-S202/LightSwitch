package com.lightswitch.core.domain.flag.repository.entity

import com.lightswitch.core.domain.flag.repository.FlagRepository
import com.lightswitch.core.domain.flag.repository.TagRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TagTest {

    @Autowired
    private lateinit var tagRepository: TagRepository

    @Test
    fun `tag 저장 테스트`() {
        // given
        val tag1 = Tag(
            colorHex = "#FFFFFF",
            content = "test"
        )

        val tag2 = Tag(
            colorHex = "#000000",
            content = "test2"
        )

        // when
        tagRepository.saveAll(listOf(tag1, tag2))

        // then
        assertNotNull(tag1.content)
        assertNotNull(tag2.content)

        assertEquals(tag1.colorHex, "#FFFFFF")
        assertEquals(tag2.colorHex, "#000000")
    }
}