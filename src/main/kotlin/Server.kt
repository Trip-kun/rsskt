package tech.trip_kun

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.httpsredirect.*
import io.ktor.server.routing.*
import io.ktor.server.tomcat.jakarta.*
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.security.KeyStore

private val serverConfig = getConfig()
fun startServer() {
    embeddedServer(Tomcat, configure = {
        configureTomcat = {
            if (serverConfig.security.useSSL) { // If we're using SSL, we need to set up the SSL connector.
                println("Starting ssl server on port ${serverConfig.security.sslPort}")
                println("Starting non-ssl server on port ${serverConfig.port}")
                this.setPort(serverConfig.port) // Set the non-ssl port
                try { // Try to read the keystore file and set up the SSL connector.
                    val keyStorePath = Path.of(serverConfig.security.jksFile)
                    val keyStore = KeyStore.getInstance("jks")
                    keyStore.load( // Load the keystore
                        Files.newInputStream(keyStorePath),
                        serverConfig.security.jksPassword.toCharArray()
                    )
                    sslConnector(
                        keyStore,
                        serverConfig.security.keyAlias,
                        { serverConfig.security.jksPassword.toCharArray() }, // wrapper noinline function
                        { serverConfig.security.privateKeyPassword.toCharArray() }, // wrapper noinline function
                        {  // builder function
                            this.keyStorePath = File(serverConfig.security.jksFile).absoluteFile
                            this.port = serverConfig.security.sslPort
                        }
                    )
                    println("Server started on port ${serverConfig.security.sslPort}")
                } catch (e: Exception) {
                    println("Error reading security keys: ${e.message}")
                    throw e
                }
                connector { // Set up the non-ssl connector.
                    this.port = serverConfig.port
                }
            } else { // If we're not using SSL, we only need the non-ssl connector.
                println("Starting non-ssl server on port ${serverConfig.port}")
                connector {
                    this.port = serverConfig.port
                }
            }
        }
    }) {
        if (serverConfig.security.useSSL && serverConfig.security.sslRequired) { // If we're using SSL and it's required, we set up the HTTPS redirect.
            install(HttpsRedirect) {
                sslPort = serverConfig.security.sslPort
                permanentRedirect = false // We don't want to permanently redirect, as we may want to use the non-ssl port for testing. It's just really annoying to get rid of a permanent redirect.
            }

        }
        install(ContentNegotiation) { // Set up JSON serialization for the server.
            val json = Json {
                encodeDefaults = true // Encode default values.
            }
            json(json = json)
        }
        routing { // Set up the routes. This will have to be expanded as we add more functionality.
            get("/") {
                status(call)
            }
            get("/ping") {
                ping(call)
            }
        }
    }.start(wait = true)
}