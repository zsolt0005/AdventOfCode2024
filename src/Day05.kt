import java.util.Comparator

class Day05
{
    private val input: String = FileUtils.readText("Day05.txt")

    fun partOne()
    {
        val (rawRules, updates) = parseInput(input)
        val rules = processRules(rawRules)

        var pagesSum = 0
        updates.forEach { update ->
            // Last element is not needed to be checked
            for (i in (update.size - 2) downTo 0)
            {
                val page = update[i]
                val beforePage = update[i + 1]

                val pagesBefore = rules[page] ?: return@forEach
                val isBefore = pagesBefore.contains(beforePage)
                if(!isBefore) return@forEach
            }

            val middlePage = update[update.size / 2]
            pagesSum += middlePage
        }

        println("   Sum: $pagesSum")
    }

    fun partTwo()
    {
        val (rawRules, updates) = parseInput(input)
        val rules = processRules(rawRules)

        val incorrectUpdates = updates.filter { update ->
            for (i in (update.size - 2) downTo 0)
            {
                val page = update[i]
                val beforePage = update[i + 1]

                val pagesBefore = rules[page] ?: return@filter true
                val isBefore = pagesBefore.contains(beforePage)
                if(!isBefore) return@filter true
            }

            return@filter false
        }

        val correctedUpdates = incorrectUpdates.map { incorrectUpdate ->
            return@map incorrectUpdate.sortedWith(Comparator { right, left ->
                val rightIsBefore = rules[right] ?: return@Comparator -1

                return@Comparator if(rightIsBefore.contains(left)) return@Comparator 1 else -1
            })
        }

        val pagesSum = correctedUpdates.sumOf { pages -> pages[pages.size / 2] }

        println("   Sum: $pagesSum")
    }

    private fun parseInput(text: String): Pair<List<Pair<Int, Int>>, List<List<Int>>>
    {
        val rules = mutableListOf<Pair<Int, Int>>()
        val updates = mutableListOf<List<Int>>()

        val lines = text.lines()

        var isPartOne = true
        lines.forEach { line ->

            if(line.isEmpty())
            {
                isPartOne = false
                return@forEach
            }

            if(isPartOne)
            {
                val parts = line.split("|")
                rules.add(Pair(parts[0].toInt(), parts[1].toInt()))

                return@forEach
            }

            val parts = line
                .split(",")
                .map { it.toInt() }

            updates.add(parts)
        }

        return Pair(rules, updates)
    }

    private fun processRules(rawRules: List<Pair<Int, Int>>): Map<Int, Set<Int>>
    {
        val rules = mutableMapOf<Int, MutableSet<Int>>()

        rawRules.forEach { rawRule ->
            val (left, right) = rawRule

            if(!rules.containsKey(left))
            {
                rules[left] = mutableSetOf()
            }

            rules[left]!!.add(right)
        }

        return rules
    }
}