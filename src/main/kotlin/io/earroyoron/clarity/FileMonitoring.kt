package io.earroyoron.clarity

import io.earroyoron.clarity.Checks.MonitoringFileWithParameters
import java.io.BufferedReader
import java.io.File
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.LinkedTransferQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.TransferQueue
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.timer
import kotlin.concurrent.withLock

/**
 * Implements a Producer-Consumer pattern with back-pressure
 * using a simple TransferQueue.
 * A more complex solution could use the Reactive API
 * and some complete reactive implementation (RxJava,Reactor,...)
 */
class FileMonitoring(private val arguments: MonitoringFileWithParameters) {

    fun subscribe() {
        val transferQueue: TransferQueue<String> = LinkedTransferQueue()
        val executor = Executors.newFixedThreadPool(2)
        val producer = Producer(transferQueue, arguments.filename)
        val consumer = Consumer(transferQueue, arguments.fromHost, arguments.toHost)
        timer(
            name = "Scheduler Metrics on Consumer",
            daemon = false,
            initialDelay = 5000L,
            period = 3600000L, //1h
            action = { consumer.reporting() }
        )
        executor.execute(producer)
        executor.execute(consumer)
        executor.awaitTermination(5000, TimeUnit.MICROSECONDS)
        executor.shutdown()
    }
}

/**
 * Producer reading from file
 */
class Producer(
    private val transferQueue: TransferQueue<String>,
    private val filename: String) : Runnable {

    override fun run() {
        val bufferedReader: BufferedReader = File(filename).bufferedReader()

        while (true) {
            try {
                val line = bufferedReader.readLine()
                if (null != line) { //here we block for consumer with the TO
                    transferQueue.tryTransfer(line, 500, TimeUnit.MILLISECONDS)
                } else {
                    Thread.sleep(5000) // producer sleeps
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}

/**
 * Consumer doing calculations and printing report
 * with a Timer
 */
class Consumer(private val transferQueue: TransferQueue<String>,
               private val fromHost: String,
               private val toHost: String) : TimerTask() {

    /** with multiple consumers the access should be thread safe!*/
    private var toHostConnection = mutableListOf<String>()
    private var fromHostConnection = mutableListOf<String>()
    private var grouping = mutableMapOf<String, Int>()

    private val lock: Lock = ReentrantLock()
    private val linesRead = AtomicInteger()

    override fun run() {
        while (true) {
            val connection: HostConnection = transferQueue.take().toHostConnection()
            linesRead.incrementAndGet()
            lock.withLock { // while updating we cannot print
                if (connection.origin == fromHost) fromHostConnection.add(connection.target)
                if (connection.target == toHost) toHostConnection.add(connection.origin)
                val actual = grouping.getOrDefault(connection.origin, 0)
                grouping[connection.origin] = actual + 1
            }
        }
    }

    /**
     * Monitor and empty results, called by a scheduler timer from the
     * main coordinator
     */
    fun reporting() {
        println ("---LAST HOUR REPORT--- (${linesRead.getAndSet(0)} lines processed)")
        lock.withLock { // while printing we cannot update
            print("1) Connections to $toHost: ")
            println(toHostConnection.joinToString(","))
            toHostConnection = mutableListOf()
            print("2) Connections from $fromHost: ")
            println(fromHostConnection.joinToString(","))
            fromHostConnection = mutableListOf()
            print("3) Most connected: ")
            println(grouping.toList().maxBy { it.second })
            grouping = mutableMapOf()
            println("---END REPORT--- next report in 1h")
            println()
        }
    }
}

