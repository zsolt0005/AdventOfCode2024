class Day10
{
    private val input: String = FileUtils.readText("Day10.txt")

    fun partOne()
    {
        val rows = input.lines()
        val startingPoints = getStartingPoints(rows)

        val trailHeadsCount = getPossibleTrailsWithUniqueEndingsCount(rows, startingPoints)
        println("   Trailheads: $trailHeadsCount")
    }

    fun partTwo()
    {
        val rows = input.lines()
        val startingPoints = getStartingPoints(rows)
        val possibleTrails = getPossibleTrails(rows, startingPoints)

        val trailheads = possibleTrails.size
        println("   Trailheads: $trailheads")
    }

    private fun getStartingPoints(rows: List<String>): MutableList<Pair<Int, Int>>
    {
        val startingPoints = mutableListOf<Pair<Int, Int>>()

        rows.forEachIndexed { rowIndex, row ->
            row.forEachIndexed inner@{ columnIndex, column ->
                val columnValue = column.toString().toInt()
                if(columnValue != 0) return@inner

                startingPoints.add(Pair(rowIndex, columnIndex))
            }
        }

        return startingPoints
    }

    private fun getPossibleTrailsWithUniqueEndingsCount(
        rows: List<String>,
        startingPoints: MutableList<Pair<Int, Int>>
    ): Int
    {
        val allTrails = getPossibleTrails(rows, startingPoints)

        var count = 0
        val reachableEndingPoints = allTrails
            .map { trail -> Pair(trail.first(), trail.last()) }
            .toSet()

        count += reachableEndingPoints.size

        return count
    }

    private fun getPossibleTrails(
        rows: List<String>,
        startingPoints: MutableList<Pair<Int, Int>>
    ): List<List<Pair<Int, Int>>>
    {
        val allTrails = mutableListOf<List<Pair<Int, Int>>>()

        startingPoints.forEach { startingPoint ->
            val trails = mutableListOf<MutableList<Pair<Int, Int>>>()
            getTrailRecursively(startingPoint, rows, trails)

            trails.forEach { trail ->
                if(trail.size == 10) allTrails.add(trail)
            }
        }

        return allTrails
    }

    private fun getTrailRecursively(currentPosition: Pair<Int, Int>, rows: List<String>, trails: MutableList<MutableList<Pair<Int, Int>>>, index: Int = 0, lastValue: Int = -1)
    {
        val (row, column) = currentPosition
        val currentValue = rows.getOrNull(row)?.getOrNull(column)?.toString()?.toInt() ?: return

        trails.getOrElse(index) { trails.add(index, mutableListOf()) }

        if(lastValue + 1 != currentValue)
        {
            return
        }

        trails[index].add(Pair(row, column))

        if(currentValue == 9)
        {
            return
        }

        val top = Pair(row - 1, column)
        val right = Pair(row, column +1)
        val bottom = Pair(row + 1, column)
        val left = Pair(row, column - 1)

        trails.add(trails[index].toMutableList())
        getTrailRecursively(top, rows, trails, trails.size - 1, currentValue)

        trails.add(trails[index].toMutableList())
        getTrailRecursively(right, rows, trails, trails.size - 1, currentValue)

        trails.add(trails[index].toMutableList())
        getTrailRecursively(bottom, rows, trails, trails.size - 1, currentValue)

        trails.add(trails[index].toMutableList())
        getTrailRecursively(left, rows, trails, trails.size - 1, currentValue)
    }
}