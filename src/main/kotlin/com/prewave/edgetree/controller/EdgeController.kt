package com.prewave.edgetree.controller

import com.prewave.edgetree.dto.EdgeRequest
import com.prewave.edgetree.dto.EdgeResponse
import com.prewave.edgetree.exception.ApiError
import com.prewave.edgetree.service.EdgeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/edges")
@Tag(name = "Edge API", description = "Manage edges and nodes in tree structure")
class EdgeController(
    private val edgeService: EdgeService
) {

    companion object {
        private val log = LoggerFactory.getLogger(EdgeController::class.java)
    }

    @PostMapping
    @Operation(
        summary = "Create a new edge",
        description = "Adds an edge from one node to another. Prevents duplicates and cycles.",
        responses = [
            ApiResponse(responseCode = "200", description = "Edge created successfully"),
            ApiResponse(
                responseCode = "400",
                description = "Cycle detected or undecidable edge scenario",
                content = [Content(schema = Schema(implementation = ApiError::class))]
            ),
            ApiResponse(
                responseCode = "409",
                description = "Edge already exists",
                content = [Content(schema = Schema(implementation = ApiError::class))]
            )
        ]
    )
    fun createEdge(@Valid @RequestBody edgeRequest: EdgeRequest): ResponseEntity<String> {
        log.info("Request to create edge: from [${edgeRequest.fromId}] to [${edgeRequest.toId}]")
        edgeService.createEdge(edgeRequest.fromId, edgeRequest.toId)
        return ResponseEntity.ok("Edge from [${edgeRequest.fromId}] to [${edgeRequest.toId}] created successfully")
    }

    @DeleteMapping
    @Operation(
        summary = "Delete an edge",
        description = "Deletes the edge between two nodes.",
        responses = [
            ApiResponse(responseCode = "200", description = "Edge deleted successfully"),
            ApiResponse(
                responseCode = "404",
                description = "Edge not found",
                content = [Content(schema = Schema(implementation = ApiError::class))]
            )
        ]
    )
    fun deleteEdge(
        @Valid @RequestBody edgeRequest: EdgeRequest
    ): ResponseEntity<String> {
        log.info("Request to delete edge: ${edgeRequest.fromId} → ${edgeRequest.toId}")
        edgeService.deleteEdge(edgeRequest.fromId, edgeRequest.toId)
        return ResponseEntity.ok("Edge from ${edgeRequest.fromId} to ${edgeRequest.toId} deleted successfully")
    }

    @GetMapping("/{nodeId}")
    @Operation(
        summary = "Get EdgeNode by node ID",
        description = "Fetches the EdgeNode tree structure starting from the given node.",
        responses = [
            ApiResponse(responseCode = "200", description = "EdgeNode retrieved successfully"),
            ApiResponse(
                responseCode = "400",
                description = "Invalid node ID or cycle/undecidable structure",
                content = [Content(schema = Schema(implementation = ApiError::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Node not found",
                content = [Content(schema = Schema(implementation = ApiError::class))]
            )
        ]
    )
    fun getEdgeTreeByNodeId(
        @PathVariable nodeId: Int,
        @RequestParam(required = false, defaultValue = "99")
        @Parameter(description = "Maximum tree depth to retrieve") maxDepth: Int
    ): ResponseEntity<EdgeResponse> {
        log.info("Request to fetch tree from node $nodeId with maxDepth=$maxDepth")
        val edgeNode = edgeService.getEdgeNodeByNodeId(nodeId, maxDepth)
        return ResponseEntity.ok(EdgeResponse(edgeNode, edgeNode.countNodes(), edgeNode.calculateDepth()))
    }
}