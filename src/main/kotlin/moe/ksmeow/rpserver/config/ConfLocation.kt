package moe.ksmeow.rpserver.config

import java.net.URI

open class ConfLocation(_rule: String, _option: String?,
                        _tokens: ArrayList<ConfToken<*>> = ArrayList()) : ConfList("location", _tokens) {
    val rule = _rule
    val option = _option

    open fun match(url: URI): Boolean {
        when (option) {
            null -> return url.toString().indexOf(rule) != 0
            else -> return false // TODO: Other rules.
        }
    }
}