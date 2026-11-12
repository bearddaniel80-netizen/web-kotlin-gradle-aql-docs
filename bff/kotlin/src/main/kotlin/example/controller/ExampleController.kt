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
@RequestMapping("/api/examples")
class ExampleController(private val fastapi: FastapiClient) {

    private fun segment(value: String): String =
        UriUtils.encodePathSegment(value, StandardCharsets.UTF_8)

    @GetMapping("/{filename}")
    fun exampleDefault(@PathVariable filename: String): ResponseEntity<Any?> =
        fastapi.get("/api/examples/${segment(filename)}")

    @GetMapping("/list")
    fun exampleList(): ResponseEntity<Any?> =
        fastapi.get("/api/examples/list")
}
