package moe.ksmeow.rpserver.config

open class ConfToken<T>(_name: String, _value: T) {
    private val name = _name
    private val value = _value

    open fun getName(): String = name
    open fun getValue(): T = value
}