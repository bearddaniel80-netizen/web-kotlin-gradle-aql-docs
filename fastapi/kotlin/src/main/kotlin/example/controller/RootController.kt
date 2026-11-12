package example.controller

import example.security.InternalKeyVerifier
import example.service.ProcessRunner
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.nio.file.Files
import java.nio.file.Path

@RestController
class RootController(
    private val keys: InternalKeyVerifier,
    private val runner: ProcessRunner,
) {

    private val safeName = Regex("""[\w.-]+""")

    @GetMapping("/health")
    fun health() = mapOf("status" to "ok")

    @GetMapping("/api/data")
    fun data(
        @RequestHeader(value = "X-Internal-Key", required = false) key: String?,
    ): Map<String, String> {
        keys.verify(key)
        return mapOf("message" to "Hello from Spring Boot")
    }

    @GetMapping("/api/{filename}", produces = [MediaType.TEXT_HTML_VALUE])
    fun file(
        @PathVariable filename: String,
        @RequestHeader(value = "X-Internal-Key", required = false) key: String?,
    ): String {
        keys.verify(key)
        if (!safeName.matches(filename) || ".." in filename) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid filename")
        }
        val cwd = Path.of("").toAbsolutePath()
        val input = cwd.resolve("$filename.md")
        val output = cwd.resolve("$filename.html")
        runner.render(listOf("pandoc", input.toString(), "-o", output.toString()))
        return Files.readString(output)
    }
}
