package moe.ksmeow.rpserver.config

open class ConfLocation(_rule: String, _option: String?,
                        _tokens: ArrayList<ConfToken<*>> = ArrayList()) : ConfList("location", _tokens) {
    private val rule = _rule
    private val option = _option

    open fun getRule(): String = rule
    open fun getOption(): String? = option
}