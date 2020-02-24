package io.earroyoron.clarity

import io.earroyoron.clarity.Checks.MonitoringFileWithParameters
import io.earroyoron.clarity.Checks.ParsingFileWithParameters
import java.io.File

private const val HELP = """
             Usage:
             --args="--parsing inputFile init_datetime end_datetime host"
             or
             --args="--tail inputFile from_host to_host"
            """

fun main(args: Array<String>) {

    when (val arguments  = args.checkProvidedArguments()) {
        is MonitoringFileWithParameters -> {
            val fileMonitoring = FileMonitoring(arguments as MonitoringFileWithParameters)
            fileMonitoring.subscribe()
        }

        is ParsingFileWithParameters -> {
            connectionsToInPeriod(
                filename = arguments.filename,
                toHostname = arguments.toHostname,
                fromTimestamp = arguments.fromTimestamp,
                toTimestamp = arguments.toTimestamp)
            .forEach{ println(it) }
        }
        else -> println(HELP)
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













