package example.security

import java.nio.file.Files
import java.nio.file.Path

/** Reads Docker secrets from /run/secrets/{name}, falling back to an env var for local dev. */
object Secrets {
    fun read(name: String): String {
        val path = Path.of("/run/secrets", name)
        if (Files.exists(path)) {
            return Files.readString(path).trim()
        }
        return System.getenv(name) ?: error("Missing secret or env var: $name")
    }
}
