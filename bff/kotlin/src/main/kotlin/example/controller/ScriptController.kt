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
@RequestMapping("/api/scripts")
class ScriptController(private val fastapi: FastapiClient) {

    private fun segment(value: String): String =
        UriUtils.encodePathSegment(value, StandardCharsets.UTF_8)

    @GetMapping("/{filename}")
    fun scriptDefault(@PathVariable filename: String): ResponseEntity<Any?> =
        fastapi.get("/api/scripts/${segment(filename)}")

    @GetMapping("/list")
    fun scriptList(): ResponseEntity<Any?> =
        fastapi.get("/api/scripts/list")
}
