package io.earroyoron.clarity

import io.earroyoron.clarity.Checks.*
import java.io.File

fun main(args: Array<String>) {
    when (val arguments  = args.checkProvidedArguments()) {
        is ParsingFileWithParameters -> {
            val results = connectionsToInPeriod(
                filename = arguments.filename,
                toHostname = arguments.toHostname,
                fromTimestamp = arguments.fromTimestamp,
                toTimestamp = arguments.toTimestamp)
            results.forEach{ println(it) }
        }
        is InvalidNumberOfArguments -> println(
            """
             Use four arguments in parsing mode:
             --args="inputfile init_datetime end_datetime host"
            """)
        is InvalidTimeFormat -> println(
            """
             The init_datetime and end_datetime should be provided as milliseconds
             --args="inputfile init_datetime end_datetime host"
            """)
        is InvalidTimePeriod -> println(
            """
             init_datetime should be before end_datetime:
             --args="inputfile init_datetime end_datetime host"
            """)
    }
}

/**
 * This function will return the list of the hosts(names)
 * to the toHostname in the given period fromTimestamp-toTimestamp
 */
 fun connectionsToInPeriod (filename: String,
                            toHostname: String,
                            fromTimestamp: Long,
                            toTimestamp: Long): Set<String> =
    File(filename).useLines { line ->
        line.map { it.toHostConnection() }
            .filter { it.timestamp > fromTimestamp }
            .filter { it.timestamp < toTimestamp }
            .filter { it.target == toHostname }
            .map { it.origin }.toSet()
    }








