package moe.ksmeow.rpserver.http

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import moe.ksmeow.rpserver.config.ConfSet
import java.util.logging.Logger

abstract class RequestHandler(_conf: ConfSet, _errorLog: Logger, _accessLog: Logger) : HttpHandler {
    protected val conf = _conf
    protected val errorLog = _errorLog
    protected val accessLog = _accessLog

    abstract override fun handle(exchange: HttpExchange)

    fun log(exchange: HttpExchange, error: Boolean = false) {
        accessLog.info("Client IP: " + exchange.remoteAddress
                + " Local Addr: " + exchange.localAddress
                + " Method: " + exchange.requestMethod
                + " Host: " + exchange.requestHeaders["Host"]
                + " URL: " + exchange.requestURI
                + " Status: " + exchange.responseCode
                + " User-Agent: " + exchange.requestHeaders["User-Agent"])
        if (error) errorLog.info("Client IP: " + exchange.remoteAddress
                + " Local Addr: " + exchange.localAddress
                + " Method: " + exchange.requestMethod
                + " Host: " + exchange.requestHeaders["Host"]
                + " URL: " + exchange.requestURI
                + " Status: " + exchange.responseCode
                + " User-Agent: " + exchange.requestHeaders["User-Agent"])
    }

    fun errorResponse(exchange: HttpExchange, code: Int) {
        val errorPage = "<html><head><title>HTTP 1.1/$code SRPS 1.0</title></head><body>" +
                "<h1>HTTP 1.1/$code</h1>" +
                "<hr />" +
                "<p>SRPS 1.0/${System.getProperty("os.name")}</p></body></html>"
        exchange.sendResponseHeaders(code, errorPage.toByteArray().size.toLong())
        val os = exchange.responseBody
        os.write(errorPage.toByteArray())
        os.close()
        exchange.close()
    }
}