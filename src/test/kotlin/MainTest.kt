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

})

data class HostConnection(val timestamp: Long,
                          val origin: String,
                          val target: String)

fun String.toHostConnection(): HostConnection {
    val elements: List<String> = this.split(" ")
    return HostConnection(elements[0].toLong(), elements[1], elements[2])
}
