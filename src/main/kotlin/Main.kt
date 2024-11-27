package tech.trip_kun

fun main() {
    try {
        startServer()
    } catch (e: Throwable) {
        Logger.error("Error starting server: ${e.message}")
        Logger.error("Stack trace: ${e.stackTraceToString()}")
    }
}