import kotlin.math.abs

class Day02
{
    private val reports: List<List<Int>> = parseReportsLines(
        FileUtils.readLines("Day02.txt")
    )

    fun partOne()
    {
        var safeReportsCount = 0

        reports.forEach {
            val isSafe = processReportsWithoutTolerance(it)

            if(isSafe) safeReportsCount++
        }

        println("   Safe reports count: $safeReportsCount")
    }

    fun partTwo()
    {
        var safeReportsCount = 0

        reports.forEach {
            val isSafe = processReportsWithTolerance(it)
            if(isSafe) safeReportsCount++
        }

        println("   Safe reports count: $safeReportsCount")
    }

    private fun processReportsWithTolerance(reports: List<Int>): Boolean
    {
        var isSafe = processReportsWithoutTolerance(reports)
        if(isSafe) return true

        for (i in reports.indices)
        {
            val mutableReports = reports.toMutableList()
            mutableReports.removeAt(i)

            isSafe = processReportsWithoutTolerance(mutableReports)
            if(isSafe) return true
        }

        return false
    }

    private fun processReportsWithoutTolerance(reports: List<Int>): Boolean
    {
        var isIncreasing: Boolean? = null
        for (i in 1..<(reports.size))
        {
            val actual = reports[i]
            val last = reports[i - 1]

            if(i == 1) isIncreasing = actual > last
            if(isIncreasing == true && actual < last) return false
            if(isIncreasing == false && actual > last) return false

            val difference = abs(actual - last)
            if(difference == 0 || difference > 3) return false
        }

        return true
    }

    private fun parseReportsLines(lines: List<String>): List<List<Int>>
    {
        val reportsLines = mutableListOf<List<Int>>()

        lines.forEach { line ->
            val reportRecords = line
                .split(" ")
                .map { record -> record.toInt() }

            reportsLines.add(reportRecords)
        }

        return reportsLines
    }
}