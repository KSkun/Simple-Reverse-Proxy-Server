package moe.ksmeow.rpserver.config

open class ConfToken<T>(_name: String, _value: T?) {
    val name = _name
    val value = _value
}