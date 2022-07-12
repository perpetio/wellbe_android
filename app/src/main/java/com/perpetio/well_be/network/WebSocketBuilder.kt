package com.perpetio.well_be.network

import com.perpetio.well_be.data.AccountModule
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

class WebSocketBuilder(
    private val am: AccountModule
) : ApiBuilder() {
    private val token get() = authToken(am.getToken())

    val socket: HttpClient = HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json()
        }
        defaultRequest {
            url("ws://perpetioktorchat-env.eba-kpjjv4zz.eu-central-1.elasticbeanstalk.com")
            if (token.isNotEmpty()) {
                header(AUTHORIZATION, token)
            }
            contentType(ContentType.Application.Json)
        }
        install(WebSockets)
    }
}