package com.prewave.edgetree.exception

class EdgeNodeNotFoundException (nodeId: Int) : RuntimeException("EdgeNode with id [$nodeId] not found"){
}