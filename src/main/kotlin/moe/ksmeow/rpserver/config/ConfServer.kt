package moe.ksmeow.rpserver.config

open class ConfServer(_tokens: ArrayList<ConfToken<Any>>) {
    private val tokens = _tokens

    open fun getTokenList(): ArrayList<ConfToken<Any>> = tokens
}