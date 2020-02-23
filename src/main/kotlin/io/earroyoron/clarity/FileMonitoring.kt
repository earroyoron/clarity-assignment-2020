package io.earroyoron.clarity

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.io.File


fun fileMonitoring(arguments: Checks.MonitoringFileWithParameters) {
    val stream: Flow<String> = File(arguments.filename).readLines().asFlow()
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
    println("close resources?")
}


