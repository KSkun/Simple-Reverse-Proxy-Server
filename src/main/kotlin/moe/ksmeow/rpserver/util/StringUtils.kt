package moe.ksmeow.rpserver.util

class StringUtils {
    companion object {
        private const val blankChars = " \r\n"

        fun isBlank(c: Char): Boolean = c in blankChars
    }
}