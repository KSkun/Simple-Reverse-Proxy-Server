package moe.ksmeow.rpserver.http

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import moe.ksmeow.rpserver.config.ConfLocation
import moe.ksmeow.rpserver.config.ConfSet

class ReverseHandler(_conf: ConfSet) : HttpHandler {
    private val conf = _conf

    override fun handle(exchange: HttpExchange?) {
        val header = exchange!!.requestHeaders
        for (conflt in conf.getToken("location")!!) {
            val confl = conflt as ConfLocation
            if (confl.match(exchange.requestURI)) {
                for (token in confl.value!!) {
                    when (token.name) {
                        // TODO: Finish handler.
                        "proxy_set_header" -> {

                        }
                        "proxy_pass" -> {

                        }
                    }
                }
            }
        }
    }

}