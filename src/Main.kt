import kotlin.system.measureTimeMillis

fun main()
{
    val day = Day09()

    println("[Part One]")
    val partOneELapsed = measureTimeMillis {
        day.partOne()
    }
    println("   :: Elapsed time: $partOneELapsed ms")

    println()
    println("[Part Two]")
    val partTwoElapsed = measureTimeMillis {
        day.partTwo()
    }
    println("   :: Elapsed time: $partTwoElapsed ms")
}