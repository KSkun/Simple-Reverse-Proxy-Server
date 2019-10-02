package moe.ksmeow.rpserver

import moe.ksmeow.rpserver.config.RPSConfig

class RPServer {
    companion object {
        const val VERSION = "1.0"

        @JvmStatic
        fun main(args: Array<String>) {
            // TODO: Logger
            println("Simple Reverse Proxy Server $VERSION is running.")
            println("Parsing config file...")
            println(System.getProperty("user.dir"))
            RPSConfig.INSTANCE.init()
            println(RPSConfig.INSTANCE.getConfig().getTokenList())
        }
    }
}