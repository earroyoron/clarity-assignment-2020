package io.earroyoron.clarity

import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class TailingFileTests: StringSpec({

    "test" {
        runBlocking<Unit> {
            connectionsToHost(getConnections(), "Merida") shouldHaveSize 2
        }
    }

})

suspend fun getConnections(): List<HostConnection> {
    delay(10000) //waiting half an hour...
    return listOf(
        HostConnection(123123,"Albacete","Merida"),
        HostConnection(99999, "Toledo", "Soria"),
        HostConnection(324655,"Parla", "Merida")
    )
}

fun connectionsToHost(connections: List<HostConnection>,
                      target: String) =
        connections.filter { it.target==target }


