package moe.ksmeow.rpserver

import moe.ksmeow.rpserver.config.ConfServer
import moe.ksmeow.rpserver.config.RPSConfig
import moe.ksmeow.rpserver.http.HTTPServer
import java.util.logging.Logger

/**
 * A simple reverse proxy server by KSkun.
 * @author KSkun
 */
class RPServer {
    companion object {
        const val VERSION = "1.0"
        const val TIMEOUT = 10000 // default: 10s

        val log: Logger = Logger.getLogger(this::class.toString())
        val conf: RPSConfig = RPSConfig()
        val servers: ArrayList<HTTPServer> = ArrayList()

        @JvmStatic
        fun main(args: Array<String>) {
            log.info("Simple Reverse Proxy Server $VERSION is running.")

            log.info("Parsing config file...")
            conf.init()
            log.info("Config file loaded.")

            System.setProperty("sun.net.http.allowRestrictedHeaders", "true") // release restriction
            for (servert in conf.getConfig().value!!) {
                val server = servert as ConfServer
                val httpServer = HTTPServer(server)
                log.info("HTTP Server starting at " + server.value!!.get("server_name").first().value
                        + ":" + server.value!!.get("listen").first().value + "...")
                httpServer.init()
                servers.add(httpServer)
            }
            log.info("HTTP Server started.")
        }
    }
}