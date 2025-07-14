package com.prewave.edgetree.service.impl

import com.prewave.edgetree.dto.EdgeNode
import com.prewave.edgetree.repository.EdgeRepository
import com.prewave.edgetree.service.EdgeService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EdgeServiceImpl(
    private val edgeRepository: EdgeRepository
) : EdgeService {

    companion object {
        private val log = LoggerFactory.getLogger(EdgeServiceImpl::class.java)
    }

    override fun createEdge(fromId: Int, toId: Int) {
        log.info("EdgeServiceImpl: createEdge from [$fromId] to [$toId]")
        edgeRepository.createEdge(fromId, toId)
    }

    override fun deleteEdge(fromId: Int, toId: Int) {
        log.info("EdgeServiceImpl: deleteEdge from [$fromId] to [$toId]")
        edgeRepository.deleteEdge(fromId, toId)
    }

    override fun getEdgeNodeByNodeId(nodeId: Int, maxDepth: Int): EdgeNode {
        log.info("EdgeServiceImpl: get Edge node with id [$nodeId], maxDepth [$maxDepth]")
        return edgeRepository.getEdgeNodeByNodeId(nodeId, maxDepth)
    }
}