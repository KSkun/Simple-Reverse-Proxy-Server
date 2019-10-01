package moe.ksmeow.rpserver.config

open class RPSConfig {
    private val confPath = "rps.conf"
    private var servers = ArrayList<ConfServer>()

    companion object {
        val INSTANCE = RPSConfig()
    }

    open fun getServerList(): ArrayList<ConfServer> = servers

    open fun init() {
        var parser = ConfParser(confPath)
        //servers = parser.parse()
    }
}