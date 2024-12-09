class Day03
{
    private val input: String = FileUtils.readText("Day03.txt")
    private val mulRegex: Regex = Regex("""mul\(([0-9]{1,3}),([0-9]{1,3})\)""", setOf(RegexOption.MULTILINE))
    private val mulDoDontRegex: Regex = Regex("""(mul\(([0-9]{1,3}),([0-9]{1,3})\))|(do\(\))|(don't\(\))""", setOf(RegexOption.MULTILINE))

    fun partOne()
    {
        val matches = mulRegex.findAll(input)
        var total = 0

        matches.forEach { match ->
            val groups = match.groups
            val leftOperand = groups[1]!!.value.toInt()
            val rightOperand = groups[2]!!.value.toInt()

            total += (leftOperand * rightOperand)
        }

        println("   Total: $total")
    }

    fun partTwo()
    {
        val matches = mulDoDontRegex.findAll(input)
        var total = 0;

        var mulActive = true
        matches.forEach { match ->
            val groups = match.groups
            val fullMatch = groups[0]!!.value

            if(fullMatch.contains("do()"))
            {
                mulActive = true
                return@forEach
            }

            if(fullMatch.contains("don't()"))
            {
                mulActive = false
                return@forEach
            }

            if(mulActive && fullMatch.startsWith("mul"))
            {
                val leftOperand = groups[2]!!.value.toInt()
                val rightOperand = groups[3]!!.value.toInt()

                total += (leftOperand * rightOperand)
            }
        }

        println("   Total: $total")
    }
}