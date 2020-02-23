package io.earroyoron.clarity

import io.earroyoron.clarity.Checks.*
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.specs.StringSpec

class MainTests: StringSpec({

    "Test connectedInPeriodTo function" {

        val arguments = ParsingFileWithParameters(
            filename = "./src/test/resources/input-file-10000.txt",
            fromTimestamp = 1565647204351,
            toTimestamp = 1565733598341,
            toHostname = "Porcha"
        )

        val result = connectionsToInPeriod(
            arguments.filename,
            arguments.toHostname,
            arguments.fromTimestamp,
            arguments.toTimestamp
            )

        result.shouldContainExactly( listOf<String>(
                "Suzana", "Shourya", "Iker", "Oluwabukola", "Sherrilyn", "Myla", "Alisan",
                "Wei", "Jiya", "Jeylah", "Mykenzi", "Mykeal", "Shelaine", "Loreto"))
    }

})
