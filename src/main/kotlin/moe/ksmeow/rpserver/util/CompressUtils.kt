package moe.ksmeow.rpserver.util

import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.GZIPOutputStream

class CompressUtils {
    companion object {
        fun gzip(input: ByteArray): ByteArray {
            val baos = ByteArrayOutputStream()
            val gis = GZIPOutputStream(baos)
            gis.write(input)
            gis.close()
            baos.close()
            return baos.toByteArray()
        }

        fun deflate(input: ByteArray): ByteArray {
            val deflater = Deflater()
            deflater.setInput(input)
            deflater.finish()
            val baos = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            while (!deflater.finished()) {
                val len = deflater.deflate(buffer)
                baos.write(buffer, 0, len)
            }
            deflater.end()
            baos.close()
            return baos.toByteArray()
        }
    }
}