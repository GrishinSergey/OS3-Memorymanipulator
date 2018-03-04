package com.sagrishin.os


fun addSegmentsToMemory(segments: List<com.sagrishin.os.Segment>): Boolean {
    var operationResult = false
    val segmentsSize = segments.map(Segment::size).sum()
    if (segmentsSize <= memory.freeMemorySize) {
        var memoryChunkId = findChunkIdWithMinimalPossibleSize(segmentsSize)
        if (memoryChunkId == -1) {
            deFragmentation()
            memoryChunkId = memory.memoryChunks.lastIndex
        }
        segments.forEach { it ->
            memory.memoryChunks[memoryChunkId].size -= it.size
            memory.freeMemorySize -= it.size
            memory.memoryChunks.add(MemoryChunk(it.size, 0, it, false))
        }
        if (memory.memoryChunks[memoryChunkId].size == 0) {
            memory.memoryChunks.removeAt(memoryChunkId)
        }
        calculateOffset()
        operationResult = true
    }
    return operationResult
}


fun removeProcess(processName: String): Boolean {
    var operationResult = false
    memory.memoryChunks.forEach {
        if (!it.freeFlag && it.segment!!.process.name == processName) {
            it.segment = null
            it.freeFlag = true
            memory.freeMemorySize += it.size
            operationResult = true
        }
    }
    return operationResult
}


private fun findChunkIdWithMinimalPossibleSize(necessarySize: Int): Int {
    memory.memoryChunks.filter { it.freeFlag && it.size >= necessarySize }.let { chunks ->
        var minSize = MEMORY_SIZE + 1
        var foundChunkId = -1
        (0 until chunks.size).forEach { chunkId ->
            if (chunks[chunkId].size < minSize) {
                minSize = chunks[chunkId].size
                foundChunkId = chunkId
            }
        }
        return foundChunkId
    }
}


private fun deFragmentation() {
    val freeMemoryChunk = MemoryChunk(memory.freeMemorySize, 0)
    val occupiedChunks = memory.memoryChunks.filter { !it.freeFlag }
    memory.memoryChunks = (occupiedChunks + freeMemoryChunk).toMutableList()
    calculateOffset()
}


private fun calculateOffset() {
    memory.memoryChunks[0].physicAddressOffset = 0
    (1 until memory.memoryChunks.size).forEach { i ->
        val offset = memory.memoryChunks[i - 1].size + memory.memoryChunks[i - 1].physicAddressOffset
        memory.memoryChunks[i].physicAddressOffset = offset
    }
}
