package io.earroyoron.clarity

fun main() {
    println ("hello")
}

data class HostConnection(val timestamp: Long,
                          val origin: String,
                          val target: String)

fun String.toHostConnection(): HostConnection {
    val elements: List<String> = this.split(" ")
    return HostConnection(elements[0].toLong(), elements[1], elements[2])
}
