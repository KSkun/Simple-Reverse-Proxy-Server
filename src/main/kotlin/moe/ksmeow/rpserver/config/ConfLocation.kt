package moe.ksmeow.rpserver.config

import com.google.common.collect.HashMultimap
import java.net.URI

class ConfLocation(_rule: String, _option: String?, _tokens: HashMultimap<String, ConfToken<*>> = HashMultimap.create()) :
    ConfSet("location", _tokens) {
    val rule = _rule
    val option = _option

    fun match(url: URI): Boolean {
        return when (option) {
            "/" -> true
            null, "^~" -> url.toString().indexOf(rule) == 0
            "=" -> url.toString() == rule
            "~" -> Regex(rule).find(url.toString()) != null
            "~*" -> Regex(rule.toLowerCase()).find(url.toString().toLowerCase()) != null
            else -> throw ConfigInvalidException("No such location option $option.")
        }
    }
}