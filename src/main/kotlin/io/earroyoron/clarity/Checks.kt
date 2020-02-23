package io.earroyoron.clarity

import io.earroyoron.clarity.Checks.*

/**
 * This package contains utilities to validate and manage
 * the command line parameters
 */
sealed class Checks {
    data class ParsingFileWithParameters(val filename: String,
                                         val fromTimestamp: Long,
                                         val toTimestamp: Long,
                                         val toHostname: String) : Checks()
    data class MonitoringFileWithParameters(val filename: String,
                                            val fromHost: String,
                                            val toHost: String) : Checks()
    object InvalidArguments: Checks()
    object InvalidTimeFormat: Checks()
    object InvalidTimePeriod: Checks()
}

/**
 * Checks arguments and return an object wrapping them to run the tool.
 */
fun Array<String>.checkProvidedArguments(): Checks {
    return when {
        ((this[0] == "--tail") && (this.size == 4)) -> this.getMonitoringParameters()
        ((this[0] == "--parse") && (this.size == 5))-> this.getParsingParameters()
        else -> InvalidArguments
    }
}

/**
 * Checks parameters when the tool is used with --parse option
 */
fun Array<String>.getParsingParameters(): Checks =
    if ( null == this[2].toLongOrNull() || null == this[3].toLongOrNull()  )
        InvalidTimeFormat
    else if ( this[2] > this[3] )
        InvalidTimePeriod
    else ParsingFileWithParameters(
        filename = this[1],
        fromTimestamp = this[2].toLong(),
        toTimestamp = this[3].toLong(),
        toHostname = this[4])

fun Array<String>.getMonitoringParameters(): Checks =
    MonitoringFileWithParameters(
        filename = this[1],
        fromHost = this[2],
        toHost = this[3]
       )

