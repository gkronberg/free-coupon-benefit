package com.mercadolibre.freecouponbenefit.utils

/**
 * Gets the message of the exception.
 * If it doesn't have a message (for example NullPointerException), build one with the name of the exception and part of the stackTrace.
 */
fun Throwable?.getMessage() = this?.let { ExceptionUtils.getMessage(this) }

/**
 * Returns a String with the same stackTrace as Throwable does, but truncated to 4 line breaks.
 */
fun Throwable?.truncatedStackTrace() = this?.let { ExceptionUtils.truncatedStackTrace(this) }

/**
 * Returns a String with the same stackTrace as Throwable does, but truncated to the number of line breaks passed as a parameter.
 */
fun Throwable?.truncatedStackTrace(cant: Int) = this?.let { ExceptionUtils.truncatedStackTrace(this, cant) }

object ExceptionUtils {

    /**
     * Gets the message of the exception.
     * If it doesn't have a message (for example NullPointerException), build one with the name of the exception and part of the stackTrace.
     */
    fun getMessage(e: Throwable): String {
        var msg: String
        if (!e.message.isNullOrEmpty()) {
            msg = e.message!!
        } else if (!e.localizedMessage.isNullOrEmpty()) {
            msg = e.localizedMessage
        } else {
            // This else is for example, for NullPointerException, that hasn't message or localizedMessage.
            msg = e.javaClass.name
            if (e.stackTrace != null && e.stackTrace.size > 0) {
                msg += ": " + e.stackTrace[0].toString()
            }
        }
        return msg
    }

    /**
     * Returns a String with the same stackTrace as Throwable does,
     * but truncated to N line breaks, or truncated to the number of line breaks passed as a parameter.
     */
    fun truncatedStackTrace(e: Throwable, cant: Int = 4): String {
        val sb = StringBuilder()
        if (e.stackTrace != null) {
            val msg = getMessage(e)

            sb.append("\n${e.javaClass.name}: $msg")

            for (i in 0 until cant) {
                if (e.stackTrace.size > i) {
                    sb.append("\n\tat ").append(e.stackTrace[i].toString())
                }
            }
        }
        return sb.toString()
    }
}
