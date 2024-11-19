package tech.trip_kun

import io.ktor.server.engine.*
import io.ktor.server.routing.*
import io.ktor.server.tomcat.jakarta.*


fun startServer() {
    embeddedServer(Tomcat, port = 8080) {
        routing {
            get("/") {
                status(call)
            }
            get("/ping") {
                ping(call)
            }
        }
    }.start(wait = true)
}