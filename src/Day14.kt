import data.Vector
import kotlin.math.abs

class Day14
{
    private val input: String = FileUtils.readText("Day14.txt")
    private val area = Vector(100, 102) // Indexed from 0

    fun partOne()
    {
        val robots = parseRobots(input.lines())
        for (i in 0 ..< 100) elapseSecond(robots)

        val safetyFactor = calculateSafetyFactor(robots)
        println("   Safety factor: $safetyFactor")
    }

    fun partTwo()
    {
        val robots = parseRobots(input.lines())

        var secondsElapsed = 0;
        for (i in 0 ..< 10000)
        {
            elapseSecond(robots)
            secondsElapsed++

            val areaWithRobots = getAreaWithRobots(robots, false)
            val hasXmasTree = areaWithRobots.count { it.contains("\\d{10}".toRegex()) } > 0
            if(hasXmasTree) break
        }

        println("   Seconds elapsed: $secondsElapsed")
    }

    private fun elapseSecond(robots: List<Robot>)
    {
        robots.forEach { robot ->
            var newX = robot.position.x + robot.velocity.x
            var newY = robot.position.y + robot.velocity.y

            if(newX < 0) newX = (area.x + 1) - abs(newX)
            else if(newX > area.x) newX -= (area.x + 1)

            if(newY < 0) newY = (area.y + 1) - abs(newY)
            else if(newY > area.y) newY -= (area.y + 1)

            robot.position = Vector(newX, newY)
        }
    }

    private fun calculateSafetyFactor(robots: List<Robot>): Int
    {
        val robotsInQuadrants = mutableListOf(0, 0, 0, 0)

        val (skipX, skipY) = Vector(area.x / 2, area.y / 2)

        val q1Start = Vector(0, 0)
        val q1End = Vector(skipX - 1, skipY - 1)
        val q2Start = Vector(skipX + 1, 0)
        val q2End = Vector(area.x, skipY - 1)

        val q3Start = Vector(0, skipY + 1)
        val q3End = Vector(skipX - 1, area.y)
        val q4Start = Vector(skipX + 1, skipY + 1)
        val q4End = Vector(area.x, area.y)

        robots.forEach { robot ->
            val (x, y) = robot.position
            if(x == skipX || y == skipY) return@forEach

            if(x >= q1Start.x && x <= q1End.x && y >= q1Start.y && y <= q1End.y) robotsInQuadrants[0]++
            if(x >= q2Start.x && x <= q2End.x && y >= q2Start.y && y <= q2End.y) robotsInQuadrants[1]++
            if(x >= q3Start.x && x <= q3End.x && y >= q3Start.y && y <= q3End.y) robotsInQuadrants[2]++
            if(x >= q4Start.x && x <= q4End.x && y >= q4Start.y && y <= q4End.y) robotsInQuadrants[3]++
        }

        return robotsInQuadrants.reduce { total, numberOfRobots -> total * numberOfRobots }
    }

    private fun getAreaWithRobots(robots: List<Robot>, skipMiddle: Boolean = true): List<StringBuilder>
    {
        val output = mutableListOf<StringBuilder>()

        val (skipX, skipY) = Vector(area.x / 2, area.y / 2)
        for (y in 0 .. area.y)
        {
            output.add(StringBuilder())

            for (x in 0 .. area.x)
            {
                if(skipMiddle && (x == skipX || y == skipY))
                {
                    output[y].append(" ")
                    continue
                }

                val robotsInArea = robots.count { it.position.x == x && it.position.y == y }
                output[y].append(if(robotsInArea > 0) robotsInArea else '.')
            }
        }

        return output
    }

    private fun drawAreaWithRobots(areaWithRobots: List<StringBuilder>)
    {
        areaWithRobots.forEach { line ->
            println(line)
        }
    }

    private fun parseRobots(robotLines: List<String>): List<Robot>
    {
        val robots = mutableListOf<Robot>()

        robotLines.forEach { robotLine ->
            val (positionPart, velocityPart) = robotLine.split(" ")

            val (positionX, positionY) = positionPart
                .replace("p=", "")
                .split(",")
                .map { it.toInt() }

            val (velocityX, velocityY) = velocityPart
                .replace("v=", "")
                .split(",")
                .map { it.toInt() }

            robots.add(Robot(Vector(positionX, positionY), Vector(velocityX, velocityY)))
        }

        return robots
    }

    data class Robot(var position: Vector<Int>, var velocity: Vector<Int>)
}