package io.earroyoron.clarity

import io.earroyoron.clarity.Checks.*


sealed class Checks {
    data class InputParameters(val filename: String,
                               val fromTimestamp: Long,
                               val toTimestamp: Long,
                               val toHostname: String) : Checks()
    object InvalidNumberOfArguments: Checks()
    object InvalidTimeFormat: Checks()
    object InvalidTimePeriod: Checks()
}

fun Array<String>.checkProvidedArguments(): Checks {
    return if (this.size != 4)
        InvalidNumberOfArguments
    else if ( null == this[1].toLongOrNull() || null == this[2].toLongOrNull()  )
        InvalidTimeFormat
    else if ( this[1] > this[2] )
        InvalidTimePeriod
    else InputParameters(
        filename = this[0],
        fromTimestamp = this[1].toLong(),
        toTimestamp = this[2].toLong(),
        toHostname = this[3])
}
