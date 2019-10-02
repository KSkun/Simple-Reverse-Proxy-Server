package moe.ksmeow.rpserver.http

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import moe.ksmeow.rpserver.config.ConfSet
import java.net.InetSocketAddress

class HTTPServer(_conf: ConfSet) {
    private val conf = _conf

    fun init() {
        val server = HttpServer.create(InetSocketAddress(conf.getToken("listen")!!.first().value as Int), 0)
        val context = server.createContext("/")
        if (conf.getToken("location")!!.isEmpty()) context.handler = DefaultHandler()
        server.start()
    }
}