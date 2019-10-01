package moe.ksmeow.rpserver.util

import java.io.File
import java.io.FileOutputStream

class FileUtils {
    companion object {
        fun copy(inputUri: String, dest: File) {
            var streamIn = javaClass.getResourceAsStream(inputUri)
            var streamOut = FileOutputStream(dest)
            var buffer = ByteArray(4096)
            var readBytes: Int
            while (true) {
                readBytes = streamIn.read(buffer)
                if (readBytes == 0) break
                streamOut.write(buffer)
            }
        }
    }
}