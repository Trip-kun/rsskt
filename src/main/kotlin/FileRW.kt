package tech.trip_kun

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.DirectoryNotEmptyException
import java.nio.file.Files
import java.nio.file.Path

class FileRWException(message: String) : IOException(message)

/**
 * A very simple class for reading and writing files.
 * @param path The path to the file.
 */
class FileRW(private var path: Path) {
    /**
     * Read the contents of the file.
     * @return The contents of the file as a string.
     */
    @Throws(FileRWException::class)
    fun read(): String {
        try {
            val inputStream: InputStream = Files.newInputStream(path)
            val bytes: ByteArray = inputStream.readAllBytes()
            inputStream.close()
            return String(bytes, Charsets.UTF_8)
        } catch (e: IOException) {
            throw FileRWException("Error reading file: ${e.message}")
        } catch (e: SecurityException) {
            throw FileRWException("Error reading file: ${e.message}")
        } catch (e: UnsupportedOperationException) {
            throw FileRWException("Error reading file: ${e.message}")
        } catch (e: IllegalArgumentException) {
            throw FileRWException("Error reading file: ${e.message}")
        } catch (e: OutOfMemoryError) {
            throw FileRWException("Error reading file: ${e.message}")
        }
    }

    /**
     * Write content to the file. If the file already exists, it will be deleted.
     * @param content The content to write to the file.
     */
    @Throws(FileRWException::class)
    fun write(content: String) {
        try {
            Files.deleteIfExists(path)
            val outputStream: OutputStream = Files.newOutputStream(path)
            outputStream.write(content.toByteArray(Charsets.UTF_8))
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            throw FileRWException("Error writing file: ${e.message}")
        } catch (e: DirectoryNotEmptyException) {
            throw FileRWException("Error writing file: ${e.message}")
        } catch (e: SecurityException) {
            throw FileRWException("Error writing file: ${e.message}")
        } catch (e: UnsupportedOperationException) {
            throw FileRWException("Error writing file: ${e.message}")
        } catch (e: IllegalArgumentException) {
            throw FileRWException("Error writing file: ${e.message}")
        } catch (e: OutOfMemoryError) {
            throw FileRWException("Error writing file: ${e.message}")
        }
    }
}