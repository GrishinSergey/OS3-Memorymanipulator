package com.sagrishin.os


const val MEMORY_SIZE = 16

val memory = Memory(MEMORY_SIZE)


data class Process(
        val name: String
)

data class Segment(
        val index: Int,
        val name: String,
        val size: Int,
        val process: Process
)

data class MemoryChunk(
        var size: Int,
        var physicAddressOffset: Int,
        var segment: Segment? = null,
        var freeFlag: Boolean = true
)

data class Memory(
        var freeMemorySize: Int,
        var memoryChunks: MutableList<MemoryChunk> = mutableListOf(
                MemoryChunk(freeMemorySize, 0))
)