package io.earroyoron.clarity

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class MainTest: StringSpec({

    "Number of parameters to invoke should be 4" {
        val args = arrayOf("inputfilename", "2555", "8999", "hostname")
        args.checkProvidedArguments() shouldBe Checks.Valid
    }

    "Three parameters should return error" {
        val args = arrayOf("inputfilename", "init_date", "end_date")
        args.checkProvidedArguments() shouldBe Checks.InvalidNumberOfArguments
    }

    "Second argument should be a Long" {
        val args = arrayOf("inputfilename", "init_date", "87777", "hostname")
        args.checkProvidedArguments() shouldBe Checks.InvalidTimeFormat
    }

    "Third argument should be a Long" {
        val args = arrayOf("inputfilename", "27888", "end_date", "hostname")
        args.checkProvidedArguments() shouldBe Checks.InvalidTimeFormat
    }

    "Start should be before end in timing arguments" {
        val args = arrayOf("inputfilename", "27888", "1233", "hostname")
        args.checkProvidedArguments() shouldBe Checks.InvalidTimePeriod
    }

})


