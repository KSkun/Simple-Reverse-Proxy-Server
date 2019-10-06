package moe.ksmeow.rpserver.config

class ConfUpstreamServer(_url: String) : ConfToken<String>("upstream_server", _url) {
    val options: HashMap<String, String?> = HashMap()
}