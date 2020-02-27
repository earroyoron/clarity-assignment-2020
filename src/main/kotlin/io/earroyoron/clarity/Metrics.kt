package io.earroyoron.clarity


class Metrics(
    var connectionTargets: MutableList<String> = mutableListOf(),
    var connectionOrigins: MutableList<String> = mutableListOf(),
    var grouping: MutableMap<String, Int> = mutableMapOf()
    )
{

    fun getJoinedFromHostConnection() = connectionOrigins.joinToString(",")
    fun getJoinedToHostConnection() = connectionTargets.joinToString(",")
    fun getMaxOrigin() = grouping.toList().maxBy { it.second }

    fun incrementMeasures (connection: HostConnection, arguments: Checks.MonitoringFileWithParameters) {
        connection.apply {
            if (origin == arguments.connectionsFrom) connectionTargets.add(target)
            if (target == arguments.connectionsTo) connectionOrigins.add(origin)
            val actual = grouping.getOrDefault(origin, 0)
            grouping[origin] = actual + 1
        }
    }

}
