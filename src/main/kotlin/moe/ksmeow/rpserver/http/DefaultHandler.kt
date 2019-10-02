package moe.ksmeow.rpserver.http

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler

class DefaultHandler : HttpHandler {
    private val response = "<html><head><title>Welcome to SRPS!</title></head>" +
            "<body><h1>Simple Reverse Proxy Server is Running!</h1>" +
            "<p>This is a default page for SRPS." +
            "<p>You will see this page because there's no location config in rps.conf.</p>" +
            "<hr />" +
            "<p>SRPS 1.0/" + System.getProperty("os.name") + "</p></body></html>"

    override fun handle(exchange: HttpExchange?) {
        exchange!!.sendResponseHeaders(200, response.toByteArray().size.toLong())
        val os = exchange.responseBody
        os.write(response.toByteArray())
        os.close()
    }
}