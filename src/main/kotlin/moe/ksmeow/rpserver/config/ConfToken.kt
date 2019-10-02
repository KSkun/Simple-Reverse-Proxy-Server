package moe.ksmeow.rpserver.config

open class ConfToken<T>(_name: String, _value: T?) {
    protected val name = _name
    protected val value = _value
}