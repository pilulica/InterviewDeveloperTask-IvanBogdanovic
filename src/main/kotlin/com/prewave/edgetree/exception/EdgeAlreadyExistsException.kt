package com.prewave.edgetree.exception

class EdgeAlreadyExistsException (fromId : Int, toId: Int) : RuntimeException("Edge from [$fromId] to [$toId] already exists") {
}