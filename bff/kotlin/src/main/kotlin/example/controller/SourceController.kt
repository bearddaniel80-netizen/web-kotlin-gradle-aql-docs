package example.controller

import example.service.FastapiClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriUtils
import java.nio.charset.StandardCharsets

@RestController
@RequestMapping("/api/sources")
class SourceController(private val fastapi: FastapiClient) {

    private fun segment(value: String): String =
        UriUtils.encodePathSegment(value, StandardCharsets.UTF_8)

    @GetMapping("/{filename}")
    fun sourceDefault(@PathVariable filename: String): ResponseEntity<Any?> =
        fastapi.get("/api/sources/${segment(filename)}")

    @GetMapping("/list")
    fun sourceList(): ResponseEntity<Any?> =
        fastapi.get("/api/sources/list")
}
