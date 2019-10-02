package moe.ksmeow.rpserver.config

import moe.ksmeow.rpserver.util.FileUtils
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.lang.NullPointerException

open class ConfParser(_path: String) {
    private val path = _path
    private val defaultPath = "/rps.conf"

    open fun getPath(): String = path

    open fun parse(): ConfList {
        val confFile = File(path)
        if (!confFile.exists()) FileUtils.copy(defaultPath, confFile)
        val reader = BufferedReader(InputStreamReader(FileInputStream(confFile)))
        var str = reader.readLine()
        var num = 1 // counter of line number

        val main = ConfList("main")
        var server: ConfSet? = null
        var location: ConfLocation? = null

        while (str != null) {
            when (val res = parseToken(str, num)) {
                null -> {}
                is ConfLocation -> {
                    if(server == null || location != null)
                        throw ConfigInvalidException("A format error is occurred on Line $num")
                    location = res
                }
                is ConfSet -> {
                    if(server != null)
                        throw ConfigInvalidException("A format error is occurred on Line $num")
                    server = res
                }
                is ConfBlockEnd -> {
                    when {
                        location != null -> {
                            if (server == null) throw NullPointerException()
                            server.addToken(location)
                            location = null
                        }
                        server != null -> {
                            main.addToken(server)
                            server = null
                        }
                        else -> throw ConfigInvalidException("A format error is occurred on Line $num")
                    }
                }
                else -> {
                    when {
                        location != null -> {
                            if (server == null) throw NullPointerException()
                            location.addToken(res)
                        }
                        server != null -> server.addToken(res)
                        else -> throw ConfigInvalidException("A format error is occurred on Line $num")
                    }
                }
            }

            str = reader.readLine()
            num++
        }
        return main
    }

    open fun parseToken(line: String, num: Int): ConfToken<*>? {
        val scan = StringScanner(line)
        val name = scan.next() ?: return null
        if (name == "#") return null
        if (name == "}") return ConfBlockEnd()
        var str1 = scan.next() ?: throw ConfigInvalidException("A format error is occurred on Line $num")

        when (name) {
            "listen" -> {
                if (!str1.endsWith(';'))
                    throw ConfigInvalidException("A format error is occurred on Line $num")
                val port = Integer.valueOf(str1.substring(0, str1.length - 1))
                return ConfToken(name, port)
            }
            "error_log", "access_log", "proxy_pass", "root" -> {
                if (!str1.endsWith(';'))
                    throw ConfigInvalidException("A format error is occurred on Line $num")
                val str = str1.substring(0, str1.length - 1)
                return ConfToken(name, str)
            }
            "server_name", "index" -> {
                val list = ArrayList<String>()
                while (!str1.endsWith(';')) {
                    list.add(str1)
                    str1 = scan.next() ?: throw ConfigInvalidException("A format error is occurred on Line $num")
                }
                list.add(str1.substring(0, str1.length - 1))
                return ConfToken(name, list)
            }
            "proxy_set_header" -> {
                val str2 = scan.next() ?: throw ConfigInvalidException("A format error is occurred on Line $num")
                if (!str2.endsWith(';'))
                    throw ConfigInvalidException("A format error is occurred on Line $num")
                return ConfToken(name, Pair(str1, str2.substring(0, str2.length - 1)))
            }
            "location" -> {
                val str2 = scan.next() ?: throw ConfigInvalidException("A format error is occurred on Line $num")
                return if (str2 != "{") ConfLocation(str2, str1) else ConfLocation(str1, null)
            }
            "server" -> {
                return ConfSet("server")
            }
        }

        throw ConfigInvalidException("Unknown Token on Line $num")
    }
}