package moe.ksmeow.rpserver.http

import com.sun.net.httpserver.HttpExchange
import moe.ksmeow.rpserver.RPServer
import moe.ksmeow.rpserver.config.ConfLocation
import moe.ksmeow.rpserver.config.ConfServer
import org.apache.tika.Tika
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.logging.Logger
import java.util.zip.GZIPOutputStream

class ConditionedHandler(_conf: ConfServer, _errorLog: Logger, _accessLog: Logger) :
    RequestHandler(_conf, _errorLog, _accessLog) {

    override fun handle(exchange: HttpExchange) {
        for (location in conf.locations) {
            if (location.match(exchange.requestURI)) {
                // echo token
                for (t in location.getToken("echo")!!) {
                    RPServer.log.info(t.value as String)
                }

                if (location.value!!.get("proxy_pass").isNotEmpty()) handleReverse(exchange, location)
                else handleStatic(exchange, location)
                return
            }
        }
        errorResponse(exchange, 500)
        log(exchange, true)
    }

    private fun handleReverse(exchange: HttpExchange, location: ConfLocation) {
        val urlConnection = getHttpConnection(exchange, location)
        urlConnection.connect()

        // exception handle
        if (urlConnection.responseCode / 100 > 3) {
            errorResponse(exchange, urlConnection.responseCode)
            log(exchange, true)
            return
        }

        // set headers
        for (h in urlConnection.headerFields.entries) {
            if (h.key == null) continue
            exchange.responseHeaders.add(h.key, h.value.first())
        }
        exchange.sendResponseHeaders(urlConnection.responseCode, 0)

        // set body
        val os = exchange.responseBody
        os.write(urlConnection.inputStream.readBytes())
        os.close()

        exchange.close()
        log(exchange)
    }

    private fun handleStatic(exchange: HttpExchange, location: ConfLocation) {
        val root = location.getToken("root")!!.first().value as String
        var file = File(root + exchange.requestURI)
        if (exchange.requestURI.toString() == "/") {
            var flag = false
            for (value in location.getToken("index")!!.first().value!! as ArrayList<*>) {
                val index = File(root + "/" + value as String)
                if (index.exists()) {
                    flag = true
                    file = index
                    break
                }
            }
            if (!flag) {
                errorResponse(exchange, 404)
                log(exchange, true)
                return
            }
        }
        if (!file.exists()) {
            errorResponse(exchange, 404)
            log(exchange, true)
            return
        }

        exchange.responseHeaders.add("Content-Encoding", "gzip")
        exchange.responseHeaders.add("Content-Type", Tika().detect(file))
        exchange.responseHeaders.add("Server", "SRPS 1.0")


        // set body
        val os = exchange.responseBody
        val fis = FileInputStream(file)
        val fbytes = fis.readBytes()
        fis.close()
        val bao = ByteArrayOutputStream()
        val gis = GZIPOutputStream(bao) // GZIP compress
        gis.write(fbytes)
        gis.close()
        val gbytes = bao.toByteArray()
        bao.close()
        exchange.sendResponseHeaders(200, gbytes.size.toLong())
        os.write(gbytes)
        os.close()

        exchange.close()
        log(exchange)
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
            if (value == "\$proxy_host") value = (location.value.get("proxy_pass").first().value as String).removePrefix("http://")
            urlConnection.setRequestProperty(name, value)
        }
        return urlConnection
    }
}