class Day11
{
    private val input: String = FileUtils.readText("Day11.txt")
    private val cache = mutableMapOf<Pair<String, Int>, Long>()

    fun partOne()
    {
        val stones = parseStones(input)
        val stonesCount = performBlinks(stones, 25)
        println("   Stones count: $stonesCount")
    }

    fun partTwo()
    {
        val stones = parseStones(input)
        val stonesCount = performBlinks(stones, 75)
        println("   Stones count: $stonesCount")
    }

    private fun performBlinks(stones: List<String>, count: Int): Long
    {
        var result = 0L
        stones.forEach { stone ->
            result += performBlink(stone, count)
        }
        return result
    }

    private fun performBlink(stone: String, count: Int): Long
    {
        if(count == 0) return 1

        val search = Pair(stone, count)

        if(cache[search] != null)
        {
            return cache[search]!!
        }

        if(stone == "0")
        {
            val result = performBlink("1", count - 1)
            cache[search] = result
            return result
        }

        val stoneNumberCount = stone.count()
        if(stoneNumberCount % 2 == 0)
        {
            val leftStone = stone.substring(0, stoneNumberCount / 2)
            val rightStone = stone.substring(stoneNumberCount / 2, stoneNumberCount).toLong().toString()

            var result = 0L
            result += performBlink(leftStone, count - 1) + performBlink(rightStone, count - 1)
            cache[search] = result
            return result
        }

        val multiplyResult = (stone.toLong() * 2024).toString()
        val result = performBlink(multiplyResult, count - 1)
        cache[search] = result
        return result
    }

    private fun parseStones(text: String): MutableList<String>
    {
        return text.split(" ").toMutableList()
    }
}