import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class MainTest: StringSpec({

    "A Connection data class should have the needed properties" {
        val connection = HostConnection(1L, "Madrid", "Parla")

        connection.timestamp shouldBe 1L
        connection.origin shouldBe "Madrid"
        connection.target shouldBe "Parla"
    }
})

data class HostConnection(val timestamp: Long,
                          val origin: String,
                          val target: String)
