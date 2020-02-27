package io.earroyoron.clarity

import io.earroyoron.clarity.Checks.MonitoringFileWithParameters
import java.io.BufferedReader
import java.io.File
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.timer
import kotlin.concurrent.withLock

/**
 * Implements a Producer-Consumer pattern with back-pressure
 * using a simple TransferQueue.
 *
 * A more complex solution could use the Reactive API (Flow)
 * and some complete reactive implementation (RxJava,Reactor,...)
 */
class FileMonitoring(val arguments: MonitoringFileWithParameters) {

    var metrics = Metrics()

    val lock: Lock = ReentrantLock()
    val linesRead = AtomicInteger() // Counting read lines, for curiosity

    val transferQueue: TransferQueue<String> = LinkedTransferQueue()
    private val executor: ExecutorService = Executors.newFixedThreadPool(2)

    fun start() {

        val producer = Producer(transferQueue, arguments.filename)
        val consumer = Consumer(this)

        executor.execute(producer)
        executor.execute(consumer)
        timer(
            name = "Scheduler Metrics on Consumer",
            daemon = false,
            initialDelay = 2500, //5secs to start
            period = 3600000L, //1h
            action = { reporting() }
        )
        executor.awaitTermination(5000, TimeUnit.MICROSECONDS) //actually never
        executor.shutdown()
    }

    /**
     * Monitor and empty results, called by a scheduler timer from the
     * main coordinator
     */
    private fun reporting() {
        println ("---REPORT--- (${linesRead.getAndSet(0)} lines processed)")
        lock.withLock { // while printing we cannot update
            with(metrics) {
                print("1) Connections to $arguments.toHost: ")
                println(this.getJoinedToHostConnection())
                print("2) Connections from $arguments.fromHost: ")
                println(this.getJoinedFromHostConnection())
                print("3) Most connected: ")
                println(this.getMaxOrigin())
                println("---END REPORT--- next report in 1h")
            }
            metrics = Metrics() // just instantiate a new object
            println()
        }
    }

}

/**
 * Producer reading from file
 * If consumer do not get the line in the timeout
 * the back pressure policy is dropping it.
 */
class Producer(
    private val transferQueue: TransferQueue<String>,
    private val filename: String) : Runnable {

    override fun run() {
        val bufferedReader: BufferedReader = File(filename).bufferedReader()

        while (true) {
            try {
                val line = bufferedReader.readLine()
                if (null != line) { //here we block for consumer with the TO of 500ms
                    transferQueue.tryTransfer(line, 500, TimeUnit.MILLISECONDS)
                } else {
                    Thread.sleep(5000) // sleep some time awaiting for a new line...
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
class Consumer(private val fileMonitoring: FileMonitoring) : TimerTask() {

    override fun run() {
        while (true) {
                val connection: HostConnection = fileMonitoring.transferQueue.take().toHostConnection()
                fileMonitoring.linesRead.incrementAndGet() // Just for fun
                fileMonitoring.lock.withLock {
                    fileMonitoring.metrics.incrementMeasures(connection, fileMonitoring.arguments)
                }
//                    connection.apply {
//                        if (origin == fileMonitoring.arguments.fromHost) fileMonitoring.metrics.fromHostConnection.add(target)
//                        if (target == fileMonitoring.arguments.toHost) fileMonitoring.metrics.toHostConnection.add(origin)
//                        val actual = fileMonitoring.metrics.grouping.getOrDefault(origin, 0)
//                        fileMonitoring.metrics.grouping[origin] = actual + 1
//                    }


        }
    }




}

