class Day12
{
    private val input: String = FileUtils.readText("Day12.txt")
    private val processedChar: Char = '.'

    fun partOne()
    {
        val rows = input.lines().map { StringBuilder(it) }
        val regionsToPositionsMap = getRegionsToPositionsMap(rows)

        var price = 0
        regionsToPositionsMap.forEach { regionToPositionsMap ->
            price += calculateRegionPrice(regionToPositionsMap)
        }

        println("   Price: $price")
    }

    fun partTwo()
    {
        val rows = input.lines().map { StringBuilder(it) }
        val regionsToPositionsMap = getRegionsToPositionsMap(rows)

        var price = 0
        regionsToPositionsMap.forEach { regionToPositionsMap ->
            price += calculateRegionPriceBySides(regionToPositionsMap)
        }

        println("   Price: $price")
    }

    private fun getRegionsToPositionsMap(rows: List<StringBuilder>): List<Pair<String, MutableList<Pair<Int, Int>>>>
    {
        val regionsPlantTypeToAreaAndPerimeterMap = mutableListOf<Pair<String, MutableList<Pair<Int, Int>>>>()

        rows.forEachIndexed { rowIndex, row ->
            row.forEachIndexed inner@{ columnIndex, column ->
                if(column == processedChar) return@inner

                val positions = getRegionPositionsRecursively(rows, Pair(rowIndex, columnIndex))
                regionsPlantTypeToAreaAndPerimeterMap.add(Pair(column.toString(), positions))
            }
        }

        return regionsPlantTypeToAreaAndPerimeterMap
    }

    private fun getRegionPositionsRecursively(rows: List<StringBuilder>, position: Pair<Int, Int>): MutableList<Pair<Int, Int>>
    {
        val positions = mutableListOf(position)

        val (row, column) = position
        val currentType = rows[row][column]

        if(currentType == processedChar) return mutableListOf()
        rows[row].setCharAt(column, processedChar)

        val right = Pair(row, column + 1)
        val bottom = Pair(row + 1, column)
        val left = Pair(row, column - 1)
        val top = Pair(row - 1, column)

        val rightType = rows.getOrNull(right.first)?.getOrNull(right.second)
        val bottomType = rows.getOrNull(bottom.first)?.getOrNull(bottom.second)
        val leftType = rows.getOrNull(left.first)?.getOrNull(left.second)
        val topType = rows.getOrNull(top.first)?.getOrNull(top.second)

        if(rightType != null && rightType == currentType) positions.addAll(getRegionPositionsRecursively(rows, right))
        if(bottomType != null && bottomType == currentType) positions.addAll(getRegionPositionsRecursively(rows, bottom))
        if(leftType != null && leftType == currentType) positions.addAll(getRegionPositionsRecursively(rows, left))
        if(topType != null && topType == currentType) positions.addAll(getRegionPositionsRecursively(rows, top))

        return positions
    }

    private fun calculateRegionPrice(regionToPositionsMap: Pair<String, MutableList<Pair<Int, Int>>>): Int
    {
        val (_, positions) = regionToPositionsMap

        if(positions.isEmpty()) return 0 // Should never happen
        if(positions.size == 1) return 4 // For a single area region, its always 1*4=4

        val area = positions.size
        var fences = 0
        positions.forEach { (row, column) ->
            val isRightSameType = positions.any { (sRow, sColumn) -> sRow == row && sColumn == column + 1 }
            val isBottomSameType = positions.any { (sRow, sColumn) -> sRow == row + 1 && sColumn == column }
            val isLeftSameType = positions.any { (sRow, sColumn) -> sRow == row && sColumn == column - 1 }
            val isTopSameType = positions.any { (sRow, sColumn) -> sRow == row - 1 && sColumn == column }

            var areaFences = 4
            if(isRightSameType) areaFences--
            if(isBottomSameType) areaFences--
            if(isLeftSameType) areaFences--
            if(isTopSameType) areaFences--

            fences += areaFences
        }

        return area * fences
    }

    private fun calculateRegionPriceBySides(regionToPositionsMap: Pair<String, MutableList<Pair<Int, Int>>>): Int
    {
        val (_, positions) = regionToPositionsMap
        if(positions.isEmpty()) return 0 // Should never happen
        if(positions.size == 1) return 4 // For a single area region, its always 1*4=4

        val area = positions.size

        val sides = mutableListOf<Pair<Side, Pair<Int, Int>>>()
        positions.forEach { (row, column) ->
            val isRightSameType = positions.any { (sRow, sColumn) -> sRow == row && sColumn == column + 1 }
            val isBottomSameType = positions.any { (sRow, sColumn) -> sRow == row + 1 && sColumn == column }
            val isLeftSameType = positions.any { (sRow, sColumn) -> sRow == row && sColumn == column - 1 }
            val isTopSameType = positions.any { (sRow, sColumn) -> sRow == row - 1 && sColumn == column }

            if(!isRightSameType) sides.add(Pair(Side.Right, Pair(row, column)))
            if(!isLeftSameType) sides.add(Pair(Side.Left, Pair(row, column)))

            if(!isBottomSameType) sides.add(Pair(Side.Bottom, Pair(row, column)))
            if(!isTopSameType) sides.add(Pair(Side.Top, Pair(row, column)))
        }

        val groupedVerticalSides = sides
            .filter { (side, _) -> side == Side.Right || side == Side.Left }
            .groupBy { (side, position) -> Pair(side, position.second) }
            .toMutableMap()

        val groupedHorizontalSides = sides
            .filter { (side, _) -> side == Side.Top || side == Side.Bottom }
            .groupBy { (side, position) -> Pair(side, position.first) }
            .toMutableMap()

        var lastValue: Int?
        groupedVerticalSides.forEach { (column, _) ->
            lastValue = null

            groupedVerticalSides[column] = groupedVerticalSides[column]!!
                .sortedBy { (_, position) -> position.first }
                .filter { (_, position) ->
                    val (row, _) = position

                    if(lastValue == null)
                    {
                        lastValue = row
                        return@filter true
                    }

                    if(lastValue!! + 1 == row)
                    {
                        lastValue = row
                        return@filter false
                    }

                    lastValue = row
                    return@filter true
                }
        }
        groupedHorizontalSides.forEach { (row, _) ->
            lastValue = null

            groupedHorizontalSides[row] = groupedHorizontalSides[row]!!
                .sortedBy { (_, position) -> position.second }
                .filter { (_, position) ->
                    val (_, column) = position

                    if(lastValue == null)
                    {
                        lastValue = column
                        return@filter true
                    }

                    if(lastValue!! + 1 == column)
                    {
                        lastValue = column
                        return@filter false
                    }

                    lastValue = column
                    return@filter true
                }
        }

        val cleanedVerticalSides = groupedVerticalSides.flatMap { it.value }
        val cleanedHorizontalSides = groupedHorizontalSides.flatMap { it.value }

        return area * (cleanedVerticalSides.size + cleanedHorizontalSides.size)
    }

    enum class Side
    {
        Top,
        Left,
        Bottom,
        Right
    }
}