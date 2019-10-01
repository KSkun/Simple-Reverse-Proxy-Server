package moe.ksmeow.rpserver

import moe.ksmeow.rpserver.config.RPSConfig

class RPServer {
    companion object {
        val VERSION = "1.0"
    }

    fun main() {
        // TODO: Logger
        println("Simple Reverse Proxy Server $VERSION is running.")
        println("Parsing config file...")
        RPSConfig.INSTANCE.init()
    }

}