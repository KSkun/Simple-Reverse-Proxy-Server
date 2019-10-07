package moe.ksmeow.rpserver.config

class ConfUpstreamType(_type: String) : ConfToken<String>("upstream_type", _type) {
    companion object {
        // only native types
        val DEFAULT = ConfUpstreamType("default")
        // val LEAST_CONN = ConfUpstreamType("least_conn")
        val IP_HASH = ConfUpstreamType("ip_hash")
    }
}