package tech.trip_kun

fun main() {
    try {
        startServer()
    } catch (e: Throwable) {
        println("Error starting server: ${e.message}")
        println(e.stackTrace.toString())
    }
}