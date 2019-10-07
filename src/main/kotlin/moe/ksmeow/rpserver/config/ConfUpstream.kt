package moe.ksmeow.rpserver.config

class ConfUpstream(_name: String, _type: ConfUpstreamType = ConfUpstreamType.DEFAULT) :
    ConfToken<ConfUpstreamType>("upstream", _type) {
    val servers: ArrayList<ConfUpstreamServer> = ArrayList()
    val upstreamName = _name

    // variables for polling
    private var next = 0
    private var count = 0

    fun next(): String {
        val res = next
        count++
        if(servers[next].options["weight"] == null ||
            count == Integer.valueOf(servers[next].options["weight"])) {
            next = (next + 1) % servers.size
            count = 0
        }
        return servers[res].value!!
    }
}