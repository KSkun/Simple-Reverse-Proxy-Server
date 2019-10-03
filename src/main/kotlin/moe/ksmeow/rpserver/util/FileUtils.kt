package moe.ksmeow.rpserver.util

import java.io.File
import java.lang.Exception
import java.nio.file.Files

class FileUtils {
    companion object {
        fun copyFromJar(inputUri: String, dest: File) {
            val streamIn = this::class.java.getResourceAsStream(inputUri)
            Files.copy(streamIn, dest.toPath())
        }

        fun createNewFile(file: File) {
            if (!file.exists()) {
                file.createNewFile()
                return
            }
            for (i in 1..1000000) {
                val file1 = File(file.path + "." + i)
                if (!file1.exists()) {
                    file.renameTo(file1)
                    file.createNewFile()
                    return
                }
            }
            throw TooManyFileException(file.toString() + "has too many old versions.")
        }
    }
}