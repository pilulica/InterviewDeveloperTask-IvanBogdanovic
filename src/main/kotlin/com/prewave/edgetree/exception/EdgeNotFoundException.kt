package com.prewave.edgetree.exception

class EdgeNotFoundException(fromId : Int, toId: Int) : RuntimeException("Edge from [$fromId] to [$toId] not found") {
}