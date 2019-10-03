package moe.ksmeow.rpserver.config

import com.google.common.collect.HashMultimap
import java.net.URI

open class ConfLocation(_rule: String, _option: String?, _tokens: HashMultimap<String, ConfToken<*>> = HashMultimap.create()) :
    ConfSet("location", _tokens) {
    val rule = _rule
    val option = _option

    open fun match(url: URI): Boolean {
        return when (option) {
            null, "^~" -> url.toString().indexOf(rule) == 0
            "=" -> url.toString() == rule
            "~" -> url.toString().matches(Regex(rule))
            "~*" -> url.toString().toLowerCase().matches(Regex(rule.toLowerCase()))
            else -> throw ConfigInvalidException("No such location option $option.")
        }
    }
}