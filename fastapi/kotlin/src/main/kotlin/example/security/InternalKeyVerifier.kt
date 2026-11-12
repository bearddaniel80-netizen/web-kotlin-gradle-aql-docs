package example.security

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import java.security.MessageDigest

@Component
class InternalKeyVerifier {

    private val expected = Secrets.read("fastapi_internal_key").toByteArray()

    fun verify(provided: String?) {
        if (provided == null || !MessageDigest.isEqual(expected, provided.toByteArray())) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden")
        }
    }
}
