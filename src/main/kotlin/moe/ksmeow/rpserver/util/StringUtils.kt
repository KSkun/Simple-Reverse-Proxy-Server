package moe.ksmeow.rpserver.util

class StringUtils {
    companion object {
        private const val graphChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ#$./:;?!&{}=^~*_"
        private const val blankChars = " \r\n"

        fun valid(c: Char): Boolean = c in graphChars || c in blankChars
        fun isBlank(c: Char): Boolean = c in blankChars
        fun isGraph(c: Char): Boolean = c in graphChars
    }
}