package moe.ksmeow.rpserver.http

import com.sun.net.httpserver.HttpServer
import moe.ksmeow.rpserver.config.ConfSet
import moe.ksmeow.rpserver.util.FileUtils
import java.io.File
import java.net.InetSocketAddress
import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

class HTTPServer(_conf: ConfSet) {
    private val conf = _conf

    private var errorLog: Logger = Logger.getLogger(this::class.toString() + "#error")
    private var accessLog: Logger = Logger.getLogger(this::class.toString() + "#access")

    fun init() {
        FileUtils.createNewFile(File(conf.getToken("error_log")!!.first().value as String))
        FileUtils.createNewFile(File(conf.getToken("access_log")!!.first().value as String))
        val errorFile = FileHandler(conf.getToken("error_log")!!.first().value as String)
        val accessFile = FileHandler(conf.getToken("access_log")!!.first().value as String)
        errorFile.formatter = SimpleFormatter()
        accessFile.formatter = SimpleFormatter()
        errorLog.addHandler(errorFile)
        accessLog.addHandler(accessFile)

        val server = HttpServer.create(InetSocketAddress(conf.getToken("listen")!!.first().value as Int), 0)
        val context = server.createContext("/")
        if (conf.getToken("location")!!.isEmpty()) context.handler = DefaultHandler(conf, errorLog, accessLog)
        else context.handler = ConditionedHandler(conf, errorLog, accessLog)
        server.start()
    }
}