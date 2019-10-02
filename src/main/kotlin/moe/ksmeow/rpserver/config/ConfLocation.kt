package moe.ksmeow.rpserver.config

open class ConfLocation(_rule: String, _option: String?,
                        _tokens: ArrayList<ConfToken<*>> = ArrayList()) : ConfList("location", _tokens) {
    val rule = _rule
    val option = _option
}