package moe.ksmeow.rpserver.config

class ConfMain(_servers: ArrayList<ConfServer> = ArrayList(),
               _upstreams: HashMap<String, ConfUpstream> = HashMap()) :
    ConfList("main", _servers as ArrayList<ConfToken<*>>) {
    val upstreams = _upstreams
}