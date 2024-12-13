class Day13
{
    private val input: String = FileUtils.readText("Day13.txt")
    private val machines = prepareMachines(input.lines())

    private val aCost: Long = 3
    private val bCost: Long = 1

    fun partOne()
    {
        var tokens = 0L
        machines.forEach { machine ->
            tokens += calculateMinimalTokensNeeded(machine)
        }

        println("   Tokens: $tokens")
    }

    fun partTwo()
    {
        machines.forEach { machine ->
            val (pX, pY) = machine.prize
            machine.prize = Pair(pX + 10000000000000, pY + 10000000000000)
        }

        var tokens = 0L
        machines.forEach { machine ->
            tokens += calculateMinimalTokensNeeded(machine)
        }

        println("   Tokens: $tokens")
    }

    private fun calculateMinimalTokensNeeded(machine: Machine): Long
    {
        val (targetX, targetY) = machine.prize
        val (aX, aY) = machine.buttonA
        val (bX, bY) = machine.buttonB

        val matrix = arrayOf(
            arrayOf(aX.toDouble(), bX.toDouble(), targetX.toDouble()),
            arrayOf(aY.toDouble(), bY.toDouble(), targetY.toDouble())
        )

        // Gaussian elimination
        // Step 1: Normalize the first row
        val factor1 = matrix[0][0]
        for (i in matrix[0].indices)
        {
            matrix[0][i] /= factor1
        }

        // Step 2: Eliminate the first variable from the second row
        val factor2 = matrix[1][0]
        for (i in matrix[1].indices)
        {
            matrix[1][i] -= factor2 * matrix[0][i]
        }

        // Step 3: Solve for the second variable (b)
        val bClicks = Math.round((matrix[1][2] / matrix[1][1]))

        // Step 4: Back substitute to solve for the first variable (a)
        val aClicks = Math.round(matrix[0][2] - matrix[0][1] * bClicks)

        val totalX = (aClicks * aX) + (bClicks * bX)
        val totalY = (aClicks * aY) + (bClicks * bY)
        if(totalX != targetX || totalY != targetY) return 0L

        return (aClicks * aCost) + (bClicks * bCost)
    }

    private fun prepareMachines(lines: List<String>): List<Machine>
    {
        val machines = mutableListOf<Machine>()

        var buttonA: Pair<Long, Long>? = null
        var buttonB: Pair<Long, Long>? = null
        var prize: Pair<Long, Long>? = null
        lines.forEach { line ->
            if(line.isEmpty())
            {
                machines.add(Machine(buttonA!!, buttonB!!, prize!!))
                return@forEach
            }

            val (type, values) = line.split(":")
            val (x, y) = values
                .split(",")
                .map { it.trim() }
                .map {
                    if(it.contains("X"))
                        it.replace("X+", "")
                          .replace("X=", "")
                          .toLong()

                    else
                        it.replace("Y+", "")
                          .replace("Y=", "")
                          .toLong()
                }

            if(type == "Button A") buttonA = Pair(x, y)
            if(type == "Button B") buttonB = Pair(x, y)
            if(type == "Prize") prize = Pair(x, y)
        }

        // Add the last machine
        machines.add(Machine(buttonA!!, buttonB!!, prize!!))

        return machines
    }

    data class Machine(val buttonA: Pair<Long, Long>, val buttonB: Pair<Long, Long>, var prize: Pair<Long, Long>)
}