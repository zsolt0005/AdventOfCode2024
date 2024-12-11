class Day07
{
    private val input: String = FileUtils.readText("Day07.txt")

    fun partOne()
    {
        val equations = parseEquations(input)

        var sum: Long = 0
        equations.forEach outer@{ (total, numbers) ->
            val operatorsCount = numbers.size - 1 // There is one less operator than numbers
            val operatorCombinations = generateOperatorCombinations(operatorsCount, false)

            operatorCombinations.forEach { operators ->
                val result = calc(numbers, operators)
                if(result == total)
                {
                    sum += total
                    return@outer
                }
            }
        }

        println("   Sum: $sum")
    }

    fun partTwo()
    {
        val equations = parseEquations(input)

        var sum: Long = 0
        equations.forEach outer@{ (total, numbers) ->
            val operatorsCount = numbers.size - 1 // There is one less operator than numbers
            val operatorCombinations = generateOperatorCombinations(operatorsCount, true)

            operatorCombinations.forEach { operators ->
                val result = calc(numbers, operators)
                if(result == total)
                {
                    sum += total
                    return@outer
                }
            }
        }

        println("   Sum: $sum")
    }

    private fun calc(numbers: List<Long>, operators: List<String>): Long
    {
        var operatorIndex = 0
        return numbers.reduce { total, number ->
            val operator = operators[operatorIndex]
            operatorIndex++

            return@reduce when (operator) {
                "+" -> total + number
                "*" -> total * number
                "||" -> "$total$number".toLong()
                else -> throw Exception("Unknown operator $operator")
            }
        }
    }

    private fun generateOperatorCombinations(size: Int, combineOperator: Boolean): List<List<String>>
    {
        val combinations = mutableListOf<List<String>>()

        fun helper(current: List<String>, remaining: Int)
        {
            if (remaining == 0)
            {
                combinations.add(current)
                return
            }

            helper(current + "+", remaining - 1)
            helper(current + "*", remaining - 1)
            if(combineOperator) helper(current + "||", remaining - 1)
        }

        helper(emptyList(), size)
        return combinations
    }

    private fun parseEquations(text: String): MutableList<Equation>
    {
        val equations = mutableListOf<Equation>()
        val lines = text.lines()

        lines.forEach { line ->
            val (result, rawNumbers) = line.split(": ")

            val numbers = rawNumbers
                .split(" ")
                .map { it.toLong() }

            equations.add(Equation(result.toLong(), numbers))
        }

        return equations
    }

    data class Equation(val total: Long, val numbers: List<Long>)
}