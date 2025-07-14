package com.prewave.edgetree.dto

class EdgeNode(
    val id: Int,
    val children: List<EdgeNode> = emptyList(),
) {

    fun countNodes(): Int {
        return 1 + children.sumOf { it.countNodes() }
    }

    fun calculateDepth(): Int {
        return if (children.isEmpty()) 0
        else 1 + children.maxOf { it.calculateDepth() }
    }
}