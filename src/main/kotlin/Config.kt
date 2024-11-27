package tech.trip_kun

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class Config(
    val port: Int = 8080,
    val version: String = "alpha",
    val security: SecurityConfig = SecurityConfig()
) {
    init {
        require(port in 1..65535) { "port must be between 1 and 65535" }
    }
}

@Serializable
data class SecurityConfig(
    val useSSL: Boolean = true,
    val jksFile: String = "",
    val jksPassword: String = "",
    val keyAlias: String = "",
    val privateKeyPassword: String = "",
    val sslPort: Int = 443,
    val sslRequired: Boolean = true
) {
    init {
        require(sslPort in 1..65535) { "port must be between 1 and 65535" }
        if (useSSL) {
            require(jksFile.isNotBlank()) { "jksFile must be set when useSSL is true" }
            require(jksPassword.isNotBlank()) { "jksPassword must be set when useSSL is true" }
            require(keyAlias.isNotBlank()) { "keyAlias must be set when useSSL is true" }
            require(privateKeyPassword.isNotBlank()) { "privateKeyPassword must be set when useSSL is true" }
        }
    }
}

private val json = Json { encodeDefaults = true; prettyPrint = true } // Set up JSON serialization for the Config class. This is separate from the JSON serialization for the main server as we want pretty print for the config, but not for the server.
private lateinit var config: Config
fun getConfig(): Config {
    if (!::config.isInitialized) {
        try { // First we see if the config exists and is valid JSON for the Config class.
            println("Reading config.json")
            val file = File("config.json")

            config = json.decodeFromString(file.readText())
        } catch (e: SerializationException) { // If the file is invalid JSON, we print an error and throw an exception, noting possible solutions.
            println("You may need to delete config.json and restart the server. This could be caused by a configuration update. If so, try renaming config.json to config.json.old, and restarting the server to create a new default config, then update the new config.json with your settings.")
            throw e
        } catch (e: Exception) { // If the file doesn't exist, we write a default config.
            config = Config() // Default config
            val file = File("config.json")
            try {
                println("Writing default config.json")
                file.writeText(json.encodeToString(config))
            } catch (e: Exception) { // If we can't write the default config, we print an error and throw an exception.
                println("Error writing default config: ${e.message}")
                throw e
            }
        }
    }
    return config
}