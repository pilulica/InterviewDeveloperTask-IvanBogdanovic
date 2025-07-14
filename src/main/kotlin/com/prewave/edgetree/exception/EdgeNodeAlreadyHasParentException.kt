package com.prewave.edgetree.exception

class EdgeNodeAlreadyHasParentException(
    val fromId: Int,
    val toId: Int
) : RuntimeException("Edge node [$toId] has a parent! Cannot add edge from [$fromId]"){
}