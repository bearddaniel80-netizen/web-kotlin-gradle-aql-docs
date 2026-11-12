package example.controller

import example.security.InternalKeyVerifier
import example.service.MarkdownService
import example.service.ProcessRunner
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.nio.file.Files
import java.nio.file.Path

/** Serves /api/{examples|scripts|sources}/list and /api/{folder}/{filename}. */
@RestController
@RequestMapping("/api/sources")
class SourceController(
    private val keys: InternalKeyVerifier,
    private val runner: ProcessRunner,
    private val markdown: MarkdownService,
) {

    private val safeName = Regex("""[\w.-]+""")
    private val folder = "sources"

    @GetMapping("/list")
    fun list(
        @RequestHeader(value = "X-Internal-Key", required = false) key: String?,
    ): Map<String, List<String>> {
        keys.verify(key)
        runner.render(listOf("aql", "docs"))
        val out = runner.render(listOf("ls", folder))
        val result = out.split("\n")
            .filter { it.isNotEmpty() }
            .map { it.substringBeforeLast('.', it).ifEmpty { it } }
        return mapOf("result" to result)
    }

    @GetMapping("/{filename}", produces = [MediaType.TEXT_HTML_VALUE])
    fun renderFile(
        @PathVariable filename: String,
        @RequestHeader(value = "X-Internal-Key", required = false) key: String?,
    ): String {
        keys.verify(key)
        if (!safeName.matches(filename) || ".." in filename) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid filename")
        }
        val cwd = Path.of("").toAbsolutePath()
        val input = cwd.resolve(folder).resolve("$filename.md")
        val output = cwd.resolve(folder).resolve("$filename.html")

        markdown.mdLinksToHtml(input)
        runner.render(
            listOf("pandoc", "-f", "markdown-yaml_metadata_block", input.toString(), "-o", output.toString()),
        )
        return Files.readString(output)
    }
}
