package io.earroyoron.clarity

import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class MetricsTest : StringSpec({

    "A connection from monitored host should increment counter" {
    //Given
      val metrics = Metrics()
      val hostConnection = HostConnection(timestamp = 12, origin = "Albacete", target = "Vigo")
      val params = Checks.MonitoringFileWithParameters(
          filename = "anyfilename",
          connectionsFrom = "Albacete",
          connectionsTo = "Melilla")
    //When
      metrics.incrementMeasures(hostConnection, params)
    //Then
        metrics.connectionOrigins shouldHaveSize  0
        metrics.connectionTargets.size shouldBe 1
        metrics.connectionTargets shouldContain "Vigo"
        metrics.grouping["Albacete"] shouldBe 1
  }

    "A connection from not monitored hosts should ad only to grouped" {
        //Given
        val metrics = Metrics( grouping = mutableMapOf( "Albacete" to 23 , "New York" to 1 ))

        val hostConnection = HostConnection(timestamp = 12, origin = "Albacete", target = "Barakaldo")
        val params = Checks.MonitoringFileWithParameters(
            filename = "anyfilename",
            connectionsFrom = "New York",
            connectionsTo = "Austin")
        //When
        metrics.incrementMeasures(hostConnection, params)
        //Then
        metrics.connectionOrigins shouldHaveSize  0 // line does comply with monitored target
        metrics.connectionTargets shouldHaveSize 0 // line does comply with monitored origin
        metrics.grouping["Albacete"] shouldBe 24 // Albacete increase connections
        metrics.grouping["New York"] shouldBe 1 // NY has not been incremented
    }

    "Max origin connections is properly calculated" {
        val metrics = Metrics( grouping = mutableMapOf( "Albacete" to 23 , "New York" to 1 ))
        metrics.getMaxOrigin() shouldBe Pair("Albacete",23)
    }

    //FIXME maybe return a list of Pair?
    "Max origin connections with more than one item (fix)?" {
        val metrics = Metrics( grouping = mutableMapOf( "Albacete" to 23 , "New York" to 1, "Toledo" to 23 ))
        metrics.getMaxOrigin() shouldBe Pair("Albacete",23) //What about Toledo?
    }

})
