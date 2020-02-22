package io.earroyoron.clarity

fun main(args: Array<String>) {
    if (args.checkProvidedArguments() != Checks.Valid) {
        println ("USAGE: inputfile.txt init_datetime end_datetime host")
    }
}

enum class Checks {
    Valid, InvalidNumberOfArguments, InvalidTimeFormat, InvalidTimePeriod;
}

fun Array<String>.checkProvidedArguments(): Checks {
        return if (this.size != 4)
            Checks.InvalidNumberOfArguments
        else if ( null == this[1].toLongOrNull() || null == this[2].toLongOrNull()  )
            Checks.InvalidTimeFormat
        else if ( this[1] > this[2] )
            Checks.InvalidTimePeriod
        else Checks.Valid
}

