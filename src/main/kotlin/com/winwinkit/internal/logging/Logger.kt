package com.winwinkit.internal.logging

import java.util.logging.Level
import java.util.logging.Logger as JLogger

internal object Logger {
    private val logger: JLogger = JLogger.getLogger("com.winwinkit.logger")

    var logLevel: Level
        get() = logger.level ?: Level.INFO
        set(value) {
            logger.level = value
        }

    fun debug(message: String) = logger.log(Level.FINE, message)
    fun info(message: String) = logger.log(Level.INFO, message)
    fun warning(message: String) = logger.log(Level.WARNING, message)
    fun error(message: String) = logger.log(Level.SEVERE, message)
}
