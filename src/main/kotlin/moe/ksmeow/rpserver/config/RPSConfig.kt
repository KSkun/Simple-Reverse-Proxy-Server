package moe.ksmeow.rpserver.config

import java.lang.NullPointerException

class RPSConfig {
    private val confPath = "rps.conf"
    private var main: ConfMain? = null

    fun getConfig(): ConfMain {
        if (main == null) throw NullPointerException()
        return main!!
    }

    fun init() {
        val parser = ConfParser(confPath)
        main = parser.parse()
    }
}