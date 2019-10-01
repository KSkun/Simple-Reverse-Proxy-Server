package moe.ksmeow.rpserver.config

open class ConfLocation(_rule: String, _value: ArrayList<ConfToken<Any>>) : ConfToken<ArrayList<ConfToken<Any>>>("location", _value) {
    private val rule = _rule

    open fun getRule(): String = rule
}