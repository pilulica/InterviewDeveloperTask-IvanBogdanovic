package com.prewave.edgetree.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.Positive

data class EdgeRequest(

    @field:Positive(message = "fromId must be positive")
    val fromId: Int,

    @field:Positive(message = "toId must be positive")
    val toId: Int
) {
    @AssertTrue(message = "fromId and toId must not be the same (self-loop is not allowed)")
    @Schema(hidden = true)
    fun isNotSelfLoop(): Boolean = fromId != toId
}