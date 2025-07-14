package com.prewave.edgetree.exception

class EdgeCycleDetectedException(val fromId: Int, val toId: Int) : RuntimeException("Adding edge from [$fromId] to [$toId] will create cycle") {
}