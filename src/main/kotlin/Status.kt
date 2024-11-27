package tech.trip_kun

import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable


@Serializable
data class Status(val status: String, val version: String, val databaseOnline: Boolean)

suspend fun status(call: RoutingCall) {
    // In the future, this may check more than just database/version status. Database is assumed true until we have a database.
    val status = Status("OK", "alpha", true)
    call.respond(status)
}

suspend fun ping(call: RoutingCall) {
    call.respondText("PONG")
}