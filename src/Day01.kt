import kotlin.math.abs

class Day01
{
    private val lines: List<String> = FileUtils.readLines("Day01.txt")
    private val lists: Pair<List<Int>, List<Int>> = linesAsList(lines)

    private val leftList: List<Int> = lists.first
    private val rightList: List<Int> = lists.second

    fun partOne()
    {
        val sortedLeftList = leftList.sorted()
        val sortedRightList = rightList.sorted()

        val totalCount = sortedLeftList.size
        var totalDistance = 0

        for(i in 0..<totalCount)
        {
            val left = sortedLeftList[i]
            val right = sortedRightList[i]

            totalDistance += abs(left - right)
        }

        println("   Total distance: $totalDistance")
    }

    fun partTwo()
    {
        val sortedLeftList = leftList.sorted()

        val totalCount = sortedLeftList.size
        var similarityScore = 0

        for(i in 0..<totalCount)
        {
            val left = sortedLeftList[i]
            val rightElements = rightList.filter { value -> value == left }

            similarityScore += left * rightElements.size
        }

        println("   Similarity score: $similarityScore")
    }

    private fun linesAsList(lines: List<String>): Pair<List<Int>, List<Int>>
    {
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()

        lines.forEach { line ->
            val sides = line.split("   ")

            leftList.add(sides[0].toInt())
            rightList.add(sides[1].toInt())
        }

        return Pair(leftList, rightList)
    }
}