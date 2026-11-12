package example.model

import com.fasterxml.jackson.annotation.JsonProperty

data class UploadUrlRequest(
    @param:JsonProperty("content_type") val contentType: String,
    val filename: String,
)
