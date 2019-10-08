package moe.ksmeow.rpserver.config

import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ConfUpstream(_name: String, _type: ConfUpstreamType = ConfUpstreamType.DEFAULT) :
    ConfToken<ConfUpstreamType>("upstream", _type) {
    val servers: ArrayList<ConfUpstreamServer> = ArrayList()
    val upstreamName = _name

    // variables for polling
    private var next = 0
    private var count = 0
    private val ips = HashMap<String, Int>()
    private val ipCount = HashMap<Int, Int>()

    fun next(ip: String): String {
        return when (value) {
            ConfUpstreamType.DEFAULT -> nextDefault()
            ConfUpstreamType.IP_HASH -> nextIpHash(ip)
            // ConfUpstreamType.LEAST_CONN -> nextLeastConn() // no use
            else -> throw Exception("Unknown upstream type ${value!!.value}.")
        }
    }

    private fun nextDefault(): String {
        val res = next
        count++
        if(servers[next].options["weight"] == null ||
            count == Integer.valueOf(servers[next].options["weight"])) {
            next = (next + 1) % servers.size
            count = 0
        }
        return servers[res].value!!
    }

    private fun nextIpHash(ip: String): String {
        return if (!ips.containsKey(ip)) {
            var server = 0
            for (i in 0 until servers.size) {
                if (!ipCount.containsKey(i)) ipCount[i] = 0
                if (ipCount[i]!! < ipCount[server]!!) server = i
            }
            ips[ip] = server
            ipCount[server] = ipCount[server]!! + 1
            servers[ips[ip]!!].value!!
        } else {
            servers[ips[ip]!!].value!!
        }
    }


}