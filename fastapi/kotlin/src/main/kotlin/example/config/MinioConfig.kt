package example.config

import example.security.Secrets
import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfig {

    @Bean
    fun minioClient(
        @Value("\${minio.endpoint}") endpoint: String,
        @Value("\${minio.secure}") secure: Boolean,
    ): MinioClient {
        val url = if ("://" in endpoint) endpoint else (if (secure) "https://" else "http://") + endpoint
        return MinioClient.builder()
            .endpoint(url)
            .credentials(Secrets.read("minio_app_user"), Secrets.read("minio_app_password"))
            .build()
    }
}
