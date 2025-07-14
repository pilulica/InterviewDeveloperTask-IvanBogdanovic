package com.prewave.edgetree.service

import com.prewave.edgetree.dto.EdgeNode

interface EdgeService {
    fun createEdge(fromId:Int,toId:Int)
    fun deleteEdge(fromId: Int,toId: Int)
    fun getEdgeNodeByNodeId(nodeId:Int,maxDepth: Int = 99): EdgeNode
}