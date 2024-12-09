class Day06
{
    private val input: String = FileUtils.readText("Day06.txt")

    private val mark: Char = 'X'
    private val obstacle: Char = '#'

    fun partOne()
    {
        val map = parseMap(input)
        traverseMap(map)

        val marksCount = countMarks(map)

        println("   Distinct positions: $marksCount")
    }

    fun partTwo()
    {
        val map = parseMap(input)

        val initialMap = cloneMap(map)
        val initialPosition = getPlayersInitialPosition(initialMap)

        traverseMap(initialMap)
        val possibleObstaclePositions = getAllMarksPositions(initialMap, initialPosition)

        var possibleNewObstaclePositionsCount = 0
        possibleObstaclePositions.forEach { possibleObstaclePosition ->
            val (posX, posY) = possibleObstaclePosition
            val nMap = cloneMap(map)
            nMap[posX][posY] = obstacle

            val isLooping = !traverseMap(nMap)
            if(isLooping) possibleNewObstaclePositionsCount++
        }

        println("   Possible new obstacles: $possibleNewObstaclePositionsCount")
    }

    private fun parseMap(text: String): MutableList<MutableList<Char>>
    {
        val map = mutableListOf<MutableList<Char>>()

        val lines = text.lines()
        lines.forEachIndexed { lineIndex, line ->

            if(map.getOrNull(lineIndex) == null) map.add(lineIndex, mutableListOf())

            line.forEach { char -> map[lineIndex].add(char) }
        }

        return map
    }

    private fun getPlayersInitialPosition(map: List<List<Char>>): Pair<Int, Int>
    {
        map.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, column ->
                if(column == '^')
                {
                    return Pair(rowIndex, columnIndex)
                }
            }
        }

        // Should never happen
        return Pair(0, 0)
    }

    private fun traverseMap(map: MutableList<MutableList<Char>>): Boolean
    {
        var (x, y) = getPlayersInitialPosition(map)
        var direction = Direction.Up

        // Mark initial position
        map[x][y] = mark

        var moveX = -1 // Initial direction is UP
        var moveY = 0

        val alreadyTraversed = mutableMapOf<Pair<Int, Int>, MutableList<Direction>>()

        // Travel through the map
        while (true)
        {
            // Check for end of map
            if(x + moveX < 0 || x + moveX >= map[0].size) break
            if(y + moveY < 0 || y + moveY >= map.size) break

            // Check for obstacle and change direction
            while (map[x + moveX][y + moveY] == obstacle)
            {
                direction = when (direction) {
                    Direction.Up -> Direction.Right
                    Direction.Right -> Direction.Down
                    Direction.Down -> Direction.Left
                    Direction.Left -> Direction.Up
                }

                moveX = if(direction == Direction.Up) -1 else if(direction == Direction.Down) 1 else 0
                moveY = if(direction == Direction.Left) -1 else if(direction == Direction.Right) 1 else 0
            }

            // Check if this position was already traversed in the same direction, if yes, it is a loop
            val traversedPosition = Pair(x, y)
            if(alreadyTraversed[traversedPosition] == null) alreadyTraversed[traversedPosition] = mutableListOf()
            if(alreadyTraversed[traversedPosition]!!.contains(direction)) return false
            alreadyTraversed[traversedPosition]!!.add(direction)

            // Move
            x += moveX
            y += moveY

            map[x][y] = mark
        }

        return true
    }

    private fun countMarks(map: List<List<Char>>): Int
    {
        return getAllMarksPositions(map).size
    }

    private fun getAllMarksPositions(map: List<List<Char>>, ignorePosition: Pair<Int, Int>? = null): List<Pair<Int, Int>>
    {
        val marks = mutableListOf<Pair<Int, Int>>()
        val (ignoreRow: Int?, ignoreColumn: Int?) = ignorePosition ?: Pair(null, null)

        map.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, column ->
                if(column == mark && !(rowIndex == ignoreRow && columnIndex == ignoreColumn)) marks.add(Pair(rowIndex, columnIndex))
            }
        }

        return marks
    }

    private fun cloneMap(map: MutableList<MutableList<Char>>): MutableList<MutableList<Char>>
    {
        return map
            .toMutableList()
            .map { it.toMutableList() }
            .toMutableList()
    }

    private enum class Direction
    {
        Up, Right, Down, Left
    }
}