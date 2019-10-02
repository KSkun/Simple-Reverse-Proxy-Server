package moe.ksmeow.rpserver.util

import java.io.File
import java.nio.file.Files

class FileUtils {
    companion object {
        fun copy(inputUri: String, dest: File) {
            val streamIn = this::class.java.getResourceAsStream(inputUri)
            Files.copy(streamIn, dest.toPath())
        }
    }
}