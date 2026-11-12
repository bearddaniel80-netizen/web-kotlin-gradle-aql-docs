package example.controller

import example.service.FastapiClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriUtils
import java.nio.charset.StandardCharsets

@RestController
class DefaultController(private val fastapi: FastapiClient) {

    private fun segment(value: String): String =
        UriUtils.encodePathSegment(value, StandardCharsets.UTF_8)

    // Static routes
    @GetMapping("/health")
    fun health(): ResponseEntity<Any?> = fastapi.get("/health")

    // Static routes
    @GetMapping("/api/data")
    fun data(): ResponseEntity<Any?> = fastapi.get("/api/data")

    @GetMapping("/api/{filename}")
    fun index(@PathVariable filename: String): ResponseEntity<Any?> = 
        fastapi.get("/api/${segment(filename)}")
}
