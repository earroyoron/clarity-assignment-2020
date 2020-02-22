package io.earroyoron.clarity

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class MainTest: StringSpec({

    "A Connection data class should have the needed properties" {
        val connection = HostConnection(1L, "Madrid", "Parla")

        connection.timestamp shouldBe 1L
        connection.origin shouldBe "Madrid"
        connection.target shouldBe "Parla"
    }

    "Extension function should create a HostConnection" {
        val connection = "1230 Abad Cardenal".toHostConnection()
        connection.timestamp shouldBe 1230L
        connection.origin shouldBe "Abad"
        connection.target shouldBe "Cardenal"
    }

    "Number of parameters to invoke" {
        val args = arrayOf("inputfilename", "init_date", "end_date", "hostname")
        validArguments(args) shouldBe true
    }
})


