package com.prewave.edgetree.exception

class EdgeCycleUndecidableException(
    val fromId: Int,
    val toId: Int,
    val maxDepth: Int
) : RuntimeException("Cannot determine if adding new edge [$fromId] to [$toId] will create cycle due to exceeding maxDepth: [$maxDepth]") {
}