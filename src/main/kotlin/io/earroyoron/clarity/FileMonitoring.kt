package io.earroyoron.clarity

import io.earroyoron.clarity.Checks.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.*

class FileMonitoring(private val arguments: MonitoringFileWithParameters,
                     val stream: Flow<String>) : TimerTask() {

    override fun run() {

        runBlocking {
            //while cada media hora...
            println("CONNECTIONS TO ${arguments.toHost}")
            stream
                .map { it.toHostConnection() }
                .filter { it.target == arguments.toHost }
                .map { it -> it.origin }
                .collect { println("$it -->") }

            println("CONNECTIONS FROM ${arguments.fromHost}")
            stream
                .map { it.toHostConnection() }
                .filter { it.origin == arguments.fromHost }
                .map { it -> it.target }
                .collect { println("--> $it") }

            val grouping = mutableMapOf<String, Int>()
            println("MOST CONNECTIONS FROM:")
            stream
                .map { it.toHostConnection() }
                .collect {
                    val actual = grouping.getOrDefault(it.origin, 0)
                    grouping[it.origin] = actual + 1
                }
            println(grouping.toList().maxBy { it.second })
        }
        println("... new data in a while...")
    }

}



