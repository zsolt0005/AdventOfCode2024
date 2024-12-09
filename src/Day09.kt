class Day09
{
    private val input: String = FileUtils.readText("Day09.txt")

    fun partOne()
    {
        val uncompressedMemory = uncompressMemory(input)
        val fragmentedMemory = fragmentMemory(uncompressedMemory)

        val checksum = calculateMemoryChecksum(fragmentedMemory)
        println("   Memory checksum: $checksum")
    }

    fun partTwo()
    {
        val uncompressedMemory = uncompressMemory(input)
        val fragmentedMemory = fragmentMemoryBlocks(uncompressedMemory)

        val checksum = calculateMemoryChecksum(fragmentedMemory)
        println("   Memory checksum: $checksum")
    }

    private fun uncompressMemory(memory: String): List<Pair<Int, Int>>
    {
        val uncompressedMemory = mutableListOf<Pair<Int, Int>>()

        var isFreeSpace = false
        var memoryBlockId = 0

        memory.forEach { memoryBlockRaw ->
            val memoryBlock = memoryBlockRaw.toString().toInt()

            for (i in 0 ..< memoryBlock)
            {
                val value = if(isFreeSpace) -1 else memoryBlock
                uncompressedMemory.add(Pair(memoryBlockId, value))
            }

            if(!isFreeSpace) memoryBlockId++

            isFreeSpace = !isFreeSpace
        }

        return uncompressedMemory
    }

    private fun fragmentMemory(memory: List<Pair<Int, Int>>): List<Pair<Int, Int>>
    {
        val fragmentedMemory = memory.toMutableList()
        var lastAvailableMemoryIndex = -1

        for(i in fragmentedMemory.size-1 downTo 0)
        {
            val (memoryBlockId, memoryBlockValue) = fragmentedMemory[i]
            if(memoryBlockValue == -1) continue

            val freeMemoryBlockIndex = getFirstAvailableMemoryIndex(fragmentedMemory, lastAvailableMemoryIndex)
            if(freeMemoryBlockIndex >= i || freeMemoryBlockIndex == -1) break

            lastAvailableMemoryIndex = freeMemoryBlockIndex

            fragmentedMemory[freeMemoryBlockIndex] = Pair(memoryBlockId, memoryBlockValue)
            fragmentedMemory[i] = Pair(i, -1)
        }

        return fragmentedMemory
    }

    private fun fragmentMemoryBlocks(memory: List<Pair<Int, Int>>): List<Pair<Int, Int>>
    {
        val fragmentedMemory = memory.toMutableList()

        var lastMemoryIndexProcessed: Int = memory.size - 1
        while (lastMemoryIndexProcessed > 0)
        {
            val (_, memoryBlockValue) = fragmentedMemory[lastMemoryIndexProcessed]
            if(memoryBlockValue == -1)
            {
                lastMemoryIndexProcessed--
                continue
            }

            val memoryBlockIndexesToMove = getMemoryBlockIndexesToMove(fragmentedMemory, lastMemoryIndexProcessed)
            val freeMemoryBlockIndexes = getFirstAvailableMemoryIndexes(fragmentedMemory, memoryBlockIndexesToMove.size, lastMemoryIndexProcessed)

            lastMemoryIndexProcessed -= memoryBlockIndexesToMove.size
            if(freeMemoryBlockIndexes == null) continue

            memoryBlockIndexesToMove.forEachIndexed { i, blockIndex ->
                val freeBlockIndex = freeMemoryBlockIndexes[i]

                val block = fragmentedMemory[blockIndex]

                fragmentedMemory[freeBlockIndex] = block
                fragmentedMemory[blockIndex] = Pair(blockIndex, -1)
            }
        }

        return fragmentedMemory
    }

    private fun calculateMemoryChecksum(memory: List<Pair<Int, Int>>): Long
    {
        var checksum: Long = 0

        memory.forEachIndexed { index, (memoryBlockId, memoryBlockValue) ->
            if(memoryBlockValue == -1) return@forEachIndexed

            checksum += (index * memoryBlockId)
        }

        return checksum
    }

    private fun getFirstAvailableMemoryIndex(memory: List<Pair<Int, Int>>, offset: Int): Int
    {
        if(offset == -1)
        {
            return memory.indexOfFirst { (_, value) -> value == -1 }
        }

        for(i in offset ..< memory.size)
        {
            val (_, value) = memory[i]
            if(value == -1) return i
        }

        return -1
    }

    private fun getMemoryBlockIndexesToMove(memory: List<Pair<Int, Int>>, offset: Int): List<Int>
    {
        val indexes = mutableListOf<Int>()

        val (firstIndexValue, _) = memory[offset]
        for(i in offset downTo 0)
        {
            val (indexValue, value) = memory[i]

            if(value == -1 || indexValue != firstIndexValue) break

            indexes.add(i)
        }

        indexes.sort()
        return indexes
    }

    private fun getFirstAvailableMemoryIndexes(memory: List<Pair<Int, Int>>, size: Int, offset: Int): List<Int>?
    {
        var foundFreeSpacesCount = 0
        var endIndex = -1

        for (i in 0 .. offset)
        {
            val (_, value) = memory[i]
            if(value != -1)
            {
                foundFreeSpacesCount = 0
                continue
            }

            foundFreeSpacesCount++
            if(foundFreeSpacesCount == size)
            {
                endIndex = i
                break
            }
        }

        if(endIndex == -1) return null
        val startIndex = endIndex - (size - 1)

        val indexes = mutableListOf<Int>()
        for (i in startIndex .. endIndex) indexes.add(i)

        return indexes
    }

    private fun memoryToString(memory: List<Pair<Int, Int>>): String
    {
        return memory.joinToString("") { (id, value) -> if(value == -1) "." else id.toString() }
    }
}