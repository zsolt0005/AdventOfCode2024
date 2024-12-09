class Day08
{
    private val input: String = FileUtils.readText("Day08.txt")

    fun partOne()
    {
        val lines = input.lines()
        val groupToPositionMap = getAntennaGroupToPositionsMap(lines)
        val rowsCount = lines.size
        val columnsCount = lines[0].length

        val darkSpotLocations = mutableSetOf<Pair<Int, Int>>()
        groupToPositionMap.forEach { (_, positions) ->
            val darkSpots = getDarkSpotPositions(rowsCount, columnsCount, positions)
            darkSpotLocations.addAll(darkSpots)
        }

        val darkSpotsCount = darkSpotLocations.size
        println("   AntiNodes count: $darkSpotsCount")
    }

    fun partTwo()
    {
        val lines = input.lines()
        val groupToPositionMap = getAntennaGroupToPositionsMap(lines)
        val rowsCount = lines.size
        val columnsCount = lines[0].length

        val darkSpotLocations = mutableSetOf<Pair<Int, Int>>()
        groupToPositionMap.forEach { (_, positions) ->
            val darkSpots = getDarkSpotPositionsAccountedForHarmonics(rowsCount, columnsCount, positions)
            darkSpotLocations.addAll(darkSpots)
        }

        val darkSpotsCount = darkSpotLocations.size
        println("   AntiNodes count: $darkSpotsCount")
    }

    private fun getAntennaGroupToPositionsMap(lines: List<String>): Map<String, List<Pair<Int, Int>>>
    {
        val groupToPositionMap = mutableMapOf<String, MutableList<Pair<Int, Int>>>()

        lines.forEachIndexed { rowIndex, line ->
            line.forEachIndexed inner@{ columnIndex, char ->
                if(char == '.') return@inner

                val antennaGroup = char.toString()
                if(groupToPositionMap[antennaGroup] == null) groupToPositionMap[antennaGroup] = mutableListOf()

                groupToPositionMap[antennaGroup]!!.add(Pair(rowIndex, columnIndex))
            }
        }

        return groupToPositionMap
    }

    private fun getDarkSpotPositions(rowsCount: Int, columnsCount: Int, positions: List<Pair<Int, Int>>): List<Pair<Int, Int>>
    {
        if(positions.size <= 1) return emptyList()

        val darkSpotPositions = mutableListOf<Pair<Int, Int>>()
        positions.forEachIndexed { index, position ->
            val (row, column) = position

            positions.forEachIndexed inner@{ cIndex, cPosition ->
                if(cIndex == index) return@inner
                val (cRow, cColumn) = cPosition

                val rowDiff = cRow - row
                val columnDiff = cColumn - column

                val darkSpotRow = cRow + rowDiff
                val darkSpotColumn = cColumn + columnDiff

                if(darkSpotRow < 0 || darkSpotRow >= rowsCount) return@inner
                if(darkSpotColumn < 0 || darkSpotColumn >= columnsCount) return@inner

                darkSpotPositions.add(Pair(darkSpotRow, darkSpotColumn))
            }
        }

        return darkSpotPositions
    }

    private fun getDarkSpotPositionsAccountedForHarmonics(rowsCount: Int, columnsCount: Int, positions: List<Pair<Int, Int>>): List<Pair<Int, Int>>
    {
        if(positions.size <= 1) return emptyList()

        val darkSpotPositions = mutableListOf<Pair<Int, Int>>()
        positions.forEachIndexed { index, position ->
            val (row, column) = position
            darkSpotPositions.add(Pair(row, column))

            positions.forEachIndexed inner@{ cIndex, cPosition ->
                if(cIndex == index) return@inner
                val (cRow, cColumn) = cPosition

                val rowDiff = cRow - row
                val columnDiff = cColumn - column

                var darkSpotRow: Int = cRow + rowDiff
                var darkSpotColumn: Int = cColumn + columnDiff
                while (darkSpotRow in 0..<rowsCount && darkSpotColumn in 0..<columnsCount)
                {
                    darkSpotPositions.add(Pair(darkSpotRow, darkSpotColumn))

                    darkSpotRow += rowDiff
                    darkSpotColumn += columnDiff
                }
            }
        }

        return darkSpotPositions
    }
}