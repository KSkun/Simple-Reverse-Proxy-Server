package moe.ksmeow.rpserver.http

import com.sun.net.httpserver.HttpExchange
import moe.ksmeow.rpserver.RPServer
import moe.ksmeow.rpserver.config.ConfLocation
import moe.ksmeow.rpserver.config.ConfSet
import java.net.HttpURLConnection
import java.net.URL
import java.util.logging.Logger

class ConditionedHandler(_conf: ConfSet, _errorLog: Logger, _accessLog: Logger) :
    RequestHandler(_conf, _errorLog, _accessLog) {

    override fun handle(exchange: HttpExchange) {
        log(exchange)
        for (conflt in conf.getToken("location")!!) {
            val confl = conflt as ConfLocation
            if (confl.match(exchange.requestURI)) {
                if (confl.value!!.get("proxy_pass").isNotEmpty()) handleReverse(exchange, confl)
                else handleStatic(exchange, confl)
                return
            }
        }
        // TODO: Error log.
        exchange.sendResponseHeaders(500, 0)
    }

    private fun handleReverse(exchange: HttpExchange, location: ConfLocation) {
        val urlConnection = getHttpConnection(exchange, location)
        urlConnection.connect()
        val response = urlConnection.inputStream

        // set headers
        for (h in urlConnection.headerFields.entries) {
            if (h.key == null) continue
            exchange.responseHeaders.add(h.key, h.value.first())
        }

        // set body
        val bytes = response.readBytes()
        exchange.sendResponseHeaders(urlConnection.responseCode, bytes.size.toLong())
        val os = exchange.responseBody
        os.write(bytes)
        os.close()

        exchange.close()
    }

    private fun handleStatic(exchange: HttpExchange, location: ConfLocation) {
        // TODO: Static proxy.
    }

    private fun getHttpConnection(exchange: HttpExchange, location: ConfLocation): HttpURLConnection {
        val url = URL(location.value!!.get("proxy_pass").first().value as String + exchange.requestURI)
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection

        urlConnection.connectTimeout = RPServer.TIMEOUT
        urlConnection.readTimeout = RPServer.TIMEOUT

        // set headers
        for (h in exchange.requestHeaders) {
            urlConnection.setRequestProperty(h.key, h.value.first())
        }
        for (t in location.value.get("proxy_set_header")) {
            val name = (t.value as Pair<*, *>).first as String
            var value = t.value.second as String
            if (value == "\$host") value = exchange.requestHeaders.getFirst("Host")
            urlConnection.setRequestProperty(name, value)
        }
        return urlConnection
    }
}