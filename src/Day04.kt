class Day04
{
    private val input: String = FileUtils.readText("Day04.txt")

    private val xmasHorizontalSearchRegex: Regex = Regex("""(XMAS)""")
    private val xmasHorizontalBackwardsSearchRegex: Regex = Regex("""(SAMX)""")
    private val xmasWordLength: Int = "XMAS".length

    private val masHorizontalSearchRegex: Regex = Regex("""(MAS)""")
    private val masHorizontalBackwardsSearchRegex: Regex = Regex("""(SAM)""")

    fun partOne()
    {
        val allOccurrences = findAllOccurrences()
        println("   All occurrences: $allOccurrences")
    }

    fun partTwo()
    {
        val allOccurrences = findAllCrossedOccurrences()
        println("   All occurrences: $allOccurrences")
    }

    private fun findAllOccurrences(): Int
    {
        val rows = input.lines()

        var totalHorizontal = 0
        var totalVertical = 0
        var totalDiagonal = 0
        rows.forEachIndexed row@{ rowIndex, row ->
            totalHorizontal += xmasHorizontalSearchRegex.findAll(row).count()
            totalHorizontal += xmasHorizontalBackwardsSearchRegex.findAll(row).count()

            // Horizontal and diagonals are checked from bottom to top, so we skip the first 3 lines to prevent duplicates
            if(rowIndex < (xmasWordLength - 1)) return@row
            row.forEachIndexed column@{ charIndex, char ->
                totalVertical += getVerticalOccurrences(charIndex, rowIndex, rows)
                totalDiagonal += getDiagonalOccurrences(charIndex, rowIndex, rows)
            }
        }

        println("   Total horizontal: $totalHorizontal")
        println("   Total vertical: $totalVertical")
        println("   Total diagonal: $totalDiagonal")

        return totalHorizontal + totalVertical + totalDiagonal
    }

    private fun getVerticalOccurrences(x: Int, y: Int, rows: List<String>): Int
    {
        val offset = xmasWordLength - 1

        val line = rows
            .filterIndexed { index, _ ->
                return@filterIndexed index <= y && index >= y - offset
            }
            .map {
                return@map it[x]
            }
            .joinToString("")

        return xmasHorizontalSearchRegex.findAll(line).count() + xmasHorizontalBackwardsSearchRegex.findAll(line).count()
    }

    private fun getDiagonalOccurrences(x: Int, y: Int, rows: List<String>): Int
    {
        val offset = xmasWordLength - 1
        val maxLineLength = rows.first().length

        val lines = rows.filterIndexed { index, _ -> return@filterIndexed index <= y && index >= y - offset }

        val fromLeft = x - offset
        val fromRight = x + offset

        var fromLeftText = ""
        var fromRightText = ""

        if(fromLeft >= 0)
        {
            for(lineIndex in lines.indices)
            {
                val char = lines[lineIndex].getOrNull(fromLeft + lineIndex)
                fromLeftText += char
            }
        }

        if(fromRight < maxLineLength)
        {
            for(lineIndex in lines.indices)
            {
                val char = lines[lineIndex].getOrNull(fromRight - lineIndex)
                fromRightText += char
            }
        }

        val fromLeftTotal = xmasHorizontalSearchRegex.findAll(fromLeftText).count() + xmasHorizontalBackwardsSearchRegex.findAll(fromLeftText).count()
        val fromRightTotal = xmasHorizontalSearchRegex.findAll(fromRightText).count() + xmasHorizontalBackwardsSearchRegex.findAll(fromRightText).count()

        return fromLeftTotal + fromRightTotal
    }

    private fun findAllCrossedOccurrences(): Int
    {
        var total = 0
        val offset = 1
        val rows = input.lines()

        // Skip first and last
        for(y in 1 ..< (rows.size - offset))
        {
            // Skip first and last
            for(x in 1 ..< (rows.size - offset))
            {
                val leftToRight: String = rows[y - offset][x - offset].toString() + rows[y][x].toString() + rows[y + offset][x + offset].toString()
                val rightToLeft: String = rows[y - offset][x + offset].toString() + rows[y][x].toString() + rows[y + offset][x - offset].toString()

                val fromLeftTotal = masHorizontalSearchRegex.findAll(leftToRight).count() + masHorizontalBackwardsSearchRegex.findAll(leftToRight).count()
                val fromRightTotal = masHorizontalSearchRegex.findAll(rightToLeft).count() + masHorizontalBackwardsSearchRegex.findAll(rightToLeft).count()
                val isMatch = (fromLeftTotal > 0 && fromRightTotal > 0)

                total += if(isMatch) 1 else 0
            }
        }

        return total
    }
}