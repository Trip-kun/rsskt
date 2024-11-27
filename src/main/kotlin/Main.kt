package tech.trip_kun

fun main() {
    try {
        startServer()
    } catch (e: Throwable) {
        println("Error starting server: ${e.message}")
        println("Full error: ${e.printStackTrace()}")
    }
}