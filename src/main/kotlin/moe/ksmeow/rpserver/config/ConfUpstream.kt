package moe.ksmeow.rpserver.config

class ConfUpstream(_type: ConfUpstreamType = ConfUpstreamType.DEFAULT) : ConfToken<ConfUpstreamType>("upstream", _type) {
    val servers: ArrayList<ConfUpstreamServer> = ArrayList()
}