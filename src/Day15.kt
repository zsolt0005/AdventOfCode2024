import data.Vector
import data.plus

class Day15
{
    private val inputLines: List<String> = FileUtils.readLines("Day15.txt")
    private val moves: List<Move> = parseMoves(inputLines)

    fun partOne()
    {
        val map = parseMap(inputLines)
        val refMap = Ref(map)
        doMoves(refMap, moves)

        val sum = calculateCoordinatesSum(refMap.value)
        println("   Sum of GPS coords: $sum")
    }

    fun partTwo()
    {
        val map = expandMap(parseMap(inputLines))
        val refMap = Ref(map)

        doMoves(refMap, moves)

        val sum = calculateCoordinatesSum(refMap.value)
        println("   Sum of GPS coords: $sum")
    }

    private fun doMoves(mapReference: Ref<List<StringBuilder>>, moves: List<Move>)
    {
        moves.forEach { move -> doMove(mapReference, move, getPlayerPosition(mapReference.value)) }
    }

    private fun doMove(mapReference: Ref<List<StringBuilder>>, move: Move, objectPosition: Vector<Int>): Boolean
    {
        var map = mapReference.value

        val direction = when (move)
        {
            Move.Up -> Vector(-1, 0)
            Move.Down -> Vector(1, 0)
            Move.Right -> Vector(0, 1)
            Move.Left -> Vector(0, -1)
        }

        val charAtPosition = map[objectPosition.x][objectPosition.y]
        val currentObjectPositionToCharMap = mutableMapOf<Vector<Int>, Char>()

        if(direction.x == 0) currentObjectPositionToCharMap[objectPosition] = charAtPosition
        else when (charAtPosition)
            {
                '[' ->
                {
                    currentObjectPositionToCharMap[objectPosition] = '['
                    currentObjectPositionToCharMap[objectPosition + Vector(0, 1)] = ']'
                }
                ']' ->
                {
                    currentObjectPositionToCharMap[objectPosition + Vector(0, -1)] = '['
                    currentObjectPositionToCharMap[objectPosition] = ']'
                }
                else ->
                {
                    currentObjectPositionToCharMap[objectPosition] = charAtPosition
                }
            }

        val newPositions = currentObjectPositionToCharMap.map { (objectPosition, _) -> objectPosition + direction }
        var objectsAtNewPositions = newPositions.map { map[it.x][it.y] }

        // Wall
        if(objectsAtNewPositions.any { it == '#' }) return false

        // Check for a box
        if(objectsAtNewPositions.any { it == '[' || it == ']' || it == 'O' })
        {
            val newMap = Ref(map.deepClone())

            // If the box has both positions in the newPositions, use only one, it will move both parts
            val positionsToMove = if(objectsAtNewPositions.first() == '[' && objectsAtNewPositions.last() == ']')
                listOf(newPositions.first())
            else newPositions

            positionsToMove.forEach { newPosition ->
                if(currentObjectPositionToCharMap.containsKey(newPosition)) return@forEach
                if(map[newPosition.x][newPosition.y] == '.') return@forEach

                if(!doMove(newMap, move, newPosition))
                {
                    return false
                }
            }

            mapReference.value = newMap.value
            map = mapReference.value

            objectsAtNewPositions = newPositions.map { map[it.x][it.y] }
        }

        // Empty space
        if(!objectsAtNewPositions.all { it == '.' }) return false

        currentObjectPositionToCharMap.forEach { (objectPosition, char) ->
            val realNewPosition = objectPosition + direction
            map[objectPosition.x][objectPosition.y] = '.'
            map[realNewPosition.x][realNewPosition.y] = char
        }

        return true
    }

    private fun getPlayerPosition(map: List<StringBuilder>): Vector<Int>
    {
        map.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, columnValue ->
                if(columnValue == '@') return Vector(rowIndex, columnIndex)
            }
        }

        throw IllegalStateException("No player found")
    }

    private fun calculateCoordinatesSum(map: List<StringBuilder>): Long
    {
        var sum = 0L

        map.forEachIndexed { rowIndex, row ->
            row.forEachIndexed inner@{ columnIndex, columnValue ->
                if(columnValue == 'O') sum += (rowIndex * 100) + columnIndex
                if(columnValue == '[') sum += (rowIndex * 100) + columnIndex
            }
        }

        return sum
    }

    private fun expandMap(map: List<StringBuilder>): List<StringBuilder>
    {
        val newMap = map.toMutableList()

        newMap.forEachIndexed { rowIndex, row ->
            newMap[rowIndex] = StringBuilder(
                row
                    .replace("#".toRegex(), "##")
                    .replace("O".toRegex(), "[]")
                    .replace("\\.".toRegex(), "..")
                    .replace("@".toRegex(), "@.")
            )
        }

        return newMap
    }

    private fun parseMap(inputLines: List<String>): List<StringBuilder>
    {
        val map = mutableListOf<StringBuilder>()

        inputLines.forEach { line ->
            if(line.isEmpty()) return map

            map.add(StringBuilder(line))
        }

        return emptyList()
    }

    private fun parseMoves(inputLines: List<String>): List<Move>
    {
        val moves = mutableListOf<Move>()

        var movesPart = false
        inputLines.forEach { line ->
            if(line.isEmpty())
            {
                movesPart = true
                return@forEach
            }

            if(!movesPart) return@forEach

            line.forEach { move ->
                if(move == '^') moves.add(Move.Up)
                if(move == '>') moves.add(Move.Right)
                if(move == 'v') moves.add(Move.Down)
                if(move == '<') moves.add(Move.Left)
            }
        }

        return moves
    }

    private fun List<StringBuilder>.printMap()
    {
        println()
        this.forEach { println(it) }
        println()
    }

    private fun List<StringBuilder>.deepClone(): List<StringBuilder> = this.map { StringBuilder(it) }

    enum class Move { Up, Right, Down, Left }

    class Ref<T>(var value: T)
}
