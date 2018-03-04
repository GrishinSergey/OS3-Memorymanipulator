package com.sagrishin.os


import kotlin.system.exitProcess


fun main(args: Array<String>) {
    var chr = ""
    while (chr != "4") {
        printMenu()
        chr = readLine()!!
        when (chr) {
            "1" -> addNewProcess()
            "2" -> removeProcess()
            "3" -> exitProcess(0)
        }
    }
}

private fun printMenu() {
    println("Enter code (1 2 3) for selecting menu item")
    println("1. Add process")
    println("2. Remove process")
    println("3. Exit")
    print("> ")
}


private fun addNewProcess() {
    val segments = getNewProcessWithSegments()
    if (addSegmentsToMemory(segments)) {
        println("Res: successfully, added")
    } else {
        println("Res: can't add new process")
    }
    printMemoryMap()
}


private fun removeProcess() {
    val processName = getProcessNameForRemoving()
    if (removeProcess(processName)) {
        println("Res: successfully, removed")
    } else {
        println("Res: can't error during removing")
    }
    printMemoryMap()
}


private fun printMemoryMap() {
    println("\n\nMemory state\n")
    memory.memoryChunks.forEach {
        println("chunk offset: ${it.physicAddressOffset}")
        println("size: ${it.size}")
        println("free state: ${it.freeFlag}")
        if (it.segment != null) {
            println("process: ${it.segment!!.process.name}")
            println("segment: ${it.segment!!.name}")
            println("segment shift: ${it.segment!!.index}")
            println("physic addr from virtual: ${it.physicAddressOffset + it.segment!!.index}")
//            println("physic addr from virtual: ${toHexString(it.physicAddressOffset + it.segment!!.size)}h")
        }
        println()
    }
    println("\n\n")
}


private fun getNewProcessWithSegments(): List<Segment> {
    print("Enter new Process name: ")
    val processName = readLine()
    val process = Process(processName!!)
    println("Enter segments or press \"enter\" to finish: ")
    return getProcessSegments(process)
}


private fun getProcessSegments(process: Process): MutableList<Segment> {
    val segments = mutableListOf<Segment>()
    var segmentName: String? = " "
    var i = 0
    while (segmentName != "") {
        print("Enter new Segment name: ")
        segmentName = readLine()
        print("Enter segment size: ")
        val size = readLine()
        if (segmentName != "" && size != "") {
            segments += Segment(i++, segmentName!!, size!!.toInt(), process)
        }
    }
    return segments
}


private fun getProcessNameForRemoving(): String {
    print("Enter process for removing: ")
    return readLine()!!
}
