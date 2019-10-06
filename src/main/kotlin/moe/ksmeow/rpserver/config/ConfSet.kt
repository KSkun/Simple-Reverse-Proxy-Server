package moe.ksmeow.rpserver.config

import com.google.common.collect.HashMultimap
import java.lang.NullPointerException

open class ConfSet(_name: String, _tokens: HashMultimap<String, ConfToken<*>> = HashMultimap.create()) :
    ConfToken<HashMultimap<String, ConfToken<*>>>(_name, _tokens) {

    fun getTokenList() = value?.entries()
    fun addToken(token: ConfToken<*>) {
        if (value == null) throw NullPointerException()
        value!!.put(token.name, token)
    }
    fun getToken(name: String): MutableSet<ConfToken<*>>? {
        if (value == null) throw NullPointerException()
        return value!!.get(name)
    }
}