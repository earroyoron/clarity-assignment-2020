package io.earroyoron.clarity

import io.earroyoron.clarity.Checks.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.concurrent.timer

fun main(args: Array<String>) {

    when (val arguments  = args.checkProvidedArguments()) {
        is MonitoringFileWithParameters -> {
            val stream: Flow<String> = File(arguments.filename).readLines().asFlow()
            val fileMonitoring = FileMonitoring(arguments as MonitoringFileWithParameters, stream)
            timer(
                name = "PeriodicMonitor",
                daemon = false,
                period = 3000L,
                action = { fileMonitoring.run() }
            )
        }
        is ParsingFileWithParameters -> {
            connectionsToInPeriod(
                filename = arguments.filename,
                toHostname = arguments.toHostname,
                fromTimestamp = arguments.fromTimestamp,
                toTimestamp = arguments.toTimestamp)
            .forEach{ println(it) }
        }
        else -> println(
            """
             Usage:
             --args="--parsing inputFile init_datetime end_datetime host"
             or
             --args="--tail inputFile from_host to_host"
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













