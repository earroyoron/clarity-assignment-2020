package io.earroyoron.clarity

import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class MainTests: StringSpec({

    "Test parse function" {
        val arguments = Checks.InputParameters(
            filename = "./src/test/resources/input-file-10000.txt",
            fromTimestamp = 1565647204351,
            toTimestamp = 1565733598341,
            toHostname = "Porcha"
        )

        getConnectionsInPeriod(arguments).shouldContainExactly( listOf<String>(
                "Suzana",
                "Shourya",
                "Iker",
                "Oluwabukola",
                "Sherrilyn",
                "Myla",
                "Alisan",
                "Wei",
                "Jiya",
                "Jeylah",
                "Mykenzi",
                "Mykeal",
                "Shelaine",
                "Loreto"))
    }

})
