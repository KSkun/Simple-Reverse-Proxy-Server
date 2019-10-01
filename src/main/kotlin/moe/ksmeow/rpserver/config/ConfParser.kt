package moe.ksmeow.rpserver.config

import moe.ksmeow.rpserver.util.FileUtils
import java.io.File

open class ConfParser(_path: String) {
    private val path = _path
    private val defaultPath = "rps.conf"

    open fun getPath(): String = path

    /*open fun parse(): ArrayList<ConfServer> {
        val confFile = File(path)
        if (!confFile.exists()) FileUtils.copy(defaultPath, confFile)

        // TODO: Implement parse method.
    }*/

    open fun parseToken(line: String, num: Int): ConfToken<Any> {
        val scan = StringScanner(line)
        val name = scan.next()
        val str1 = scan.next()

        when (name) {
            "listen" -> {
                if (!str1.endsWith(';') && !scan.next().endsWith(';'))
                    throw ConfigInvalidException("Unexpected EOL on Line $num")
                val port = Integer.valueOf(str1.substring(0, str1.length - 2))
                return ConfToken(name, port)
            }
            "server_name", "error_log", "access_log" -> {
                if (!str1.endsWith(';') && !scan.next().endsWith(';'))
                    throw ConfigInvalidException("Unexpected EOL on Line $num")
                var str = scan.next()
                return ConfToken(name, str)
            }
        }

        throw ConfigInvalidException("Unknown Token on Line $num")
    }
}