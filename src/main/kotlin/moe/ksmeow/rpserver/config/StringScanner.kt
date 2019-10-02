package moe.ksmeow.rpserver.config

import moe.ksmeow.rpserver.util.StringUtils

class StringScanner(_str: String) {
    private val str = _str
    private var idx = 0

    fun getString() = str

    fun next(): String? {
        if (idx >= str.length) return null
        var lst = idx
        var flag = false
        while (idx < str.length && (!flag || flag && StringUtils.isGraph(str[idx]))) {
            if (!flag && !StringUtils.isBlank(str[idx])) lst = idx
            if (!StringUtils.isBlank(str[idx])) flag = true
            if (!StringUtils.valid(str[idx]))
                throw ConfigInvalidException("Invalid character '" + str[idx] + "' shouldn't be in the config file.")
            idx++
        }
        return if (flag) str.substring(lst, idx) else null
    }
}