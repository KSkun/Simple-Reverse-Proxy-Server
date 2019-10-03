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

    fun log(exchange: HttpExchange) {
        accessLog.info("Client IP: " + exchange.remoteAddress
                + " Local Addr: " + exchange.localAddress
                + " Method: " + exchange.requestMethod
                + " Host: " + exchange.requestHeaders["Host"]
                + " URL: " + exchange.requestURI
                + " User-Agent: " + exchange.requestHeaders["User-Agent"])
    }
}