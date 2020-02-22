package io.earroyoron.clarity

import io.earroyoron.clarity.Checks.*
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class ChecksTests: StringSpec({

    "Number of parameters to invoke should be 4" {
        val args = arrayOf("inputFilename", "2555", "8999", "hostname")
        args.checkProvidedArguments() shouldBe  InputParameters("inputFilename",2555,8999,"hostname")
    }

    "Three parameters should return error" {
        val args = arrayOf("inputFilename", "init_date", "end_date")
        args.checkProvidedArguments() shouldBe InvalidNumberOfArguments
    }

    "Second argument should be a Long" {
        val args = arrayOf("inputFilename", "init_date", "87777", "hostname")
        args.checkProvidedArguments() shouldBe InvalidTimeFormat
    }

    "Third argument should be a Long" {
        val args = arrayOf("inputFilename", "27888", "end_date", "hostname")
        args.checkProvidedArguments() shouldBe InvalidTimeFormat
    }

    "Start should be before end in timing arguments" {
        val args = arrayOf("inputFilename", "27888", "1233", "hostname")
        args.checkProvidedArguments() shouldBe InvalidTimePeriod
    }

})


