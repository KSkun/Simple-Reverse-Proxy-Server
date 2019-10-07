package moe.ksmeow.rpserver.config

import moe.ksmeow.rpserver.util.FileUtils
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

class ConfParser(_path: String) {
    private val path = _path
    private val defaultPath = "/rps.conf"
    private var num = 1 // counter of line number
    private var str: String? = null

    // hard-coded dirty implementation
    fun parse(): ConfMain {
        val confFile = File(path)
        if (!confFile.exists()) FileUtils.copyFromJar(defaultPath, confFile)
        val reader = BufferedReader(InputStreamReader(FileInputStream(confFile)))
        str = reader.readLine()

        val main = ConfMain()

        while (str != null) {
            when (val res = parseToken(str!!, num)) {
                null -> {}
                is ConfServer -> main.addToken(parseServer(reader))
                is ConfUpstream -> main.upstreams[res.upstreamName] = parseUpstream(reader, res.upstreamName)
            }

            str = reader.readLine()
            num++
        }

        return main
    }

    private fun parseToken(line: String, num: Int): ConfToken<*>? {
        val scan = StringScanner(line)
        val name = scan.next() ?: return null
        if (name.startsWith('#')) return null
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
                var str2 = scan.next() ?: throw ConfigInvalidException("A format error is occurred on Line $num")
                if (str1 == "/") str2 = str1
                return if (str2 != "{") ConfLocation(str2, str1) else ConfLocation(str1, null)
            }
            "server" -> return ConfServer()
            "upstream" -> return ConfUpstream(str1)
            "echo" -> return ConfToken("echo", str1.substring(1, str1.length - 2))
        }

        throw ConfigInvalidException("Unknown Token on Line $num")
    }

    private fun parseServer(reader: BufferedReader): ConfServer {
        str = reader.readLine()
        num++

        var location: ConfLocation? = null
        val server = ConfServer()

        while (str != null) {
            when (val res = parseToken(str!!, num)) {
                null -> {}
                is ConfLocation -> {
                    if(location != null)
                        throw ConfigInvalidException("A format error is occurred on Line $num")
                    location = res
                }
                is ConfBlockEnd -> {
                    when {
                        location != null -> {
                            server.addLocation(location)
                            location = null
                        }
                        else -> {
                            server.sortLocation()
                            return server
                        }
                    }
                }
                else -> {
                    when {
                        location != null -> {
                            location.addToken(res)
                        }
                        else -> {
                            server.addToken(res)
                        }
                    }
                }
            }

            str = reader.readLine()
            num++
        }

        throw ConfigInvalidException("Unexpected end of file.")
    }

    // because of server token has the same name with the above one, here deal with it separatedly.
    private fun parseUpstream(reader: BufferedReader, name: String): ConfUpstream {
        str = reader.readLine()
        num++

        val upstream = ConfUpstream(name)

        while (str != null) {
            val scan = StringScanner(str!!)
            when (scan.next()) {
                null -> {}
                "server" -> {
                    var url = scan.next()
                    if (url!!.endsWith(';')) url = url.substring(0, url.length - 1)
                    val server = ConfUpstreamServer(url)
                    var str2 = scan.next()
                    while (str2 != null) {
                        if (str2.endsWith(';')) str2 = str2.substring(0, str2.length - 1)
                        val index = str2.indexOf('=')
                        if (index == -1) server.options[str2] = null
                        else server.options[str2.substring(0, index)] = str2.substring(index + 1, str2.length)
                        str2 = scan.next()
                    }
                    upstream.servers.add(server)
                }
                "least_conn;" -> upstream.value = ConfUpstreamType.LEAST_CONN
                "ip_hash;" -> upstream.value = ConfUpstreamType.IP_HASH
                "}" -> return upstream
                else -> throw ConfigInvalidException("Unknown token $str.")
            }

            str = reader.readLine()
            num++
        }

        throw ConfigInvalidException("Unexpected end of file.")
    }
}