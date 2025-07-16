package com.prewave.edgetree.repository

import com.prewave.edgetree.dto.EdgeNode
import com.prewave.edgetree.exception.EdgeAlreadyExistsException
import com.prewave.edgetree.exception.EdgeCycleDetectedException
import com.prewave.edgetree.exception.EdgeCycleUndecidableException
import com.prewave.edgetree.exception.EdgeNodeAlreadyHasParentException
import com.prewave.edgetree.exception.EdgeNodeNotFoundException
import com.prewave.edgetree.exception.EdgeNotFoundException
import com.prewave.edgetree.generated.tables.references.EDGE
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.inline
import org.jooq.impl.DSL.name
import org.jooq.impl.DSL.select
import org.jooq.impl.DSL.table
import org.jooq.impl.SQLDataType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository

@Repository
class EdgeRepository(
    private val dslContext: DSLContext,
    @Value("\${edge-node.max-cycle-depth:50}")
    private val maxCycleDepth: Int,
) {
    companion object {
        private val log = LoggerFactory.getLogger(EdgeRepository::class.java)
    }

    fun createEdge(fromId: Int, toId: Int) {
        log.info("Creating edge from [$fromId] to [$toId]")

        val edgeAlreadyExists = dslContext.fetchExists(
            EDGE.where(EDGE.FROM_ID.eq(fromId).and(EDGE.TO_ID.eq(toId)))
        )

        if (edgeAlreadyExists) {
            log.warn("Edge from [$fromId] to [$toId] already exists")
            throw EdgeAlreadyExistsException(fromId, toId)
        }

        val edgeAlreadyHasParent = dslContext.fetchExists(
            EDGE.where(EDGE.TO_ID.eq(toId))
        )

        if (edgeAlreadyHasParent) {
            log.warn("Node [$toId] already has a parent, cannot add edge from [$fromId]")
            throw EdgeNodeAlreadyHasParentException(fromId, toId)
        }

        val isCycle = wouldCreateCycle(fromId, toId, maxCycleDepth)
        if (isCycle) {
            log.warn("Adding edge from [$fromId] to [$toId] will create cycle. Create skipped")
            throw EdgeCycleDetectedException(fromId, toId)
        }

        dslContext.insertInto(EDGE)
            .set(EDGE.FROM_ID, fromId)
            .set(EDGE.TO_ID, toId)
            .execute()

        log.info("Edge from [$fromId] to [$toId] successfully created")
    }

    private fun wouldCreateCycle(fromId: Int, toId: Int, maxDepth: Int): Boolean {
        log.debug("Checking for potential cycle if added edge from [$fromId] to [$toId]")

        val descendants = name("descendants")
        val toIdField = field("to_id", Int::class.java)
        val depthField = field("depth", Int::class.java)

        val children = select(EDGE.TO_ID.`as`("to_id"), inline(1).`as`("depth"))
            .from(EDGE)
            .where(EDGE.FROM_ID.eq(toId))

        val recursive = select(
            EDGE.TO_ID.`as`("to_id"),
            DSL.field("d.depth + 1", SQLDataType.INTEGER).`as`("depth")
        ).from(EDGE)
            .join(table(descendants).`as`("d"))
            .on(EDGE.FROM_ID.eq(field(name("d", "to_id"), Int::class.java)))
            .where(field(name("d", "depth"), Int::class.java).lt(maxDepth))

        val cte = descendants.`as`(children.unionAll(recursive))

        val descendantsResult = dslContext.withRecursive(cte)
            .select(toIdField, depthField)
            .from(table("descendants"))
            .fetch()

        val maxDepthReached = descendantsResult.any { it.get(depthField) == maxDepth }
        val allDescendants = descendantsResult.map { it.get(toIdField) }

        if (maxDepthReached) {
            log.warn("Max depth [$maxDepth] reached for edge from [$fromId] to [$toId] during cycle checking")
            throw EdgeCycleUndecidableException(fromId, toId, maxDepth)
        }

        val result = fromId in allDescendants

        log.debug("Cycle check from [$fromId] to [$toId] result: $result")
        return result
    }

    fun deleteEdge(fromId: Int, toId: Int) {
        log.info("Deleting edge from [$fromId] to [$toId]")
        val result = dslContext.deleteFrom(EDGE)
            .where(EDGE.FROM_ID.eq(fromId))
            .and(EDGE.TO_ID.eq(toId))
            .execute()

        if (result == 0) {
            log.warn("Edge from [$fromId] to [$toId] not found for deletion")
            throw EdgeNotFoundException(fromId, toId)
        }
        log.info("Edge from [$fromId] to [$toId] successfully deleted")
    }

    fun getEdgeNodeByNodeId(nodeId: Int, maxDepth: Int = 99): EdgeNode {
        log.info("Fetching edge node from [$nodeId] with depth [$maxDepth]")

        if (!edgeNodeExists(nodeId)) {
            throw EdgeNodeNotFoundException(nodeId)
        }

        val edgeMap = buildEdgeMapRecursiveCTE(nodeId, maxDepth)
        val edgeNode = buildEdgeNode(nodeId, edgeMap)

        log.info("Edge node for node [$nodeId] fetched successfully")
        return edgeNode
    }

    private fun edgeNodeExists(nodeId: Int): Boolean {
        return (dslContext.selectCount()
            .from(EDGE)
            .where(EDGE.FROM_ID.eq(nodeId).or(EDGE.TO_ID.eq(nodeId)))
            .fetchOne(0, Int::class.java) ?: 0) > 0
    }

    private fun buildEdgeMapRecursiveCTE(nodeId: Int, maxDepth: Int): Map<Int, List<Int>> {
        log.debug("Building edge map from [$nodeId] via recursive CTE with maxDepth [$maxDepth]")
        val cteName = name("edge_map")
        val e = EDGE.`as`("e")

        val start = select(
            e.FROM_ID.`as`("from_id"),
            e.TO_ID.`as`("to_id"),
            inline(1).`as`("depth")
        ).from(e)
            .where(e.FROM_ID.eq(nodeId))

        val recursive = select(
            e.FROM_ID.`as`("from_id"),
            e.TO_ID.`as`("to_id"),
            field("em.depth + 1", SQLDataType.INTEGER).`as`("depth")
        ).from(e)
            .join(table(cteName).`as`("em"))
            .on(e.FROM_ID.eq(field(name("em", "to_id"), Int::class.java)))
            .where(field(name("em", "depth"), Int::class.java).lt(maxDepth))

        val cte = cteName.`as`(start.unionAll(recursive))

        val fromIdField = field("from_id", Int::class.java)
        val toIdField = field("to_id", Int::class.java)

        val result = dslContext.withRecursive(cte)
            .select(fromIdField, toIdField)
            .from(table("edge_map"))
            .fetch()

        log.debug("Recursive edge map fetched with ${result.size} edges")

        return result.groupBy(
            keySelector = { it.get(fromIdField) },
            valueTransform = { it.get(toIdField) }
        )
    }

    private fun buildEdgeNode(nodeId: Int, edgeMap: Map<Int, List<Int>>): EdgeNode {
        val childrenIds = edgeMap[nodeId] ?: emptyList()
        log.debug("Building EdgeNode for [$nodeId] and all children [size: ${childrenIds.size}]")

        val children = childrenIds.map { childId ->
            buildEdgeNode(childId, edgeMap)
        }

        return EdgeNode(nodeId, children)
    }
}
