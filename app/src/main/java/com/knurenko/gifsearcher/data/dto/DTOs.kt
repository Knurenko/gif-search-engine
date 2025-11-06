package com.knurenko.gifsearcher.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Knurenko Bogdan 06.11.2025
 */
@Serializable
data class ApiResponse(
    val data: List<GifDto>, // Replace `Any` with a specific type if you know the structure of items in "data"
    val meta: Meta,
    val pagination: Pagination?
)

@Serializable
data class Meta(
    val status: Int,
    val msg: String,
    @SerialName("response_id")
    val responseId: String
)

@Serializable
data class Pagination(
    @SerialName("total_count")
    val totalCount: Int,
    val count: Int,
    val offset: Int
)

@Serializable
data class GifDto(
    val id: String,
    val url: String,
    val source: String,
    val title: String,
    @SerialName("alt_text")
    val description: String,
    val rating: String,
)