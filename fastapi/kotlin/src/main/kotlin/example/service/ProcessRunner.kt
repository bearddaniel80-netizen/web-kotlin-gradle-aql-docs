package example.service

import org.springframework.stereotype.Service
import java.io.IOException

@Service
class ProcessRunner {

    /** Runs a command in the working directory and returns its stdout (stderr is discarded). */
    fun render(command: List<String>): String =
        try {
            val process = ProcessBuilder(command)
                .redirectError(ProcessBuilder.Redirect.DISCARD)
                .start()
            val out = process.inputStream.readAllBytes().toString(Charsets.UTF_8)
            process.waitFor()
            out
        } catch (e: IOException) {
            ""
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            ""
        }
}
