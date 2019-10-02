package moe.ksmeow.rpserver.config

import java.lang.NullPointerException

open class RPSConfig {
    private val confPath = "rps.conf"
    private var main: ConfList? = null

    companion object {
        val INSTANCE = RPSConfig()
    }

    open fun getConfig(): ConfList {
        if (main == null) throw NullPointerException()
        return main!!
    }

    open fun init() {
        val parser = ConfParser(confPath)
        main = parser.parse()
    }
}