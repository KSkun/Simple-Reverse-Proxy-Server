package moe.ksmeow.rpserver.config

import java.lang.NullPointerException

class ConfList(_name: String, _tokens: ArrayList<ConfToken<*>> = ArrayList()) :
    ConfToken<ArrayList<ConfToken<*>>>(_name, _tokens) {

    fun addToken(token: ConfToken<*>) {
        if (value == null) throw NullPointerException()
        value!!.add(token)
    }
}