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
        context.handler = HTTPHandler()
        server.start()
    }

    class HTTPHandler : HttpHandler {
        override fun handle(exchange: HttpExchange?) {
            val response = "Hello, World!"
            exchange!!.sendResponseHeaders(200, response.toByteArray().size.toLong())
            val os = exchange.responseBody
            os.write(response.toByteArray())
            os.close()
        }
    }
}