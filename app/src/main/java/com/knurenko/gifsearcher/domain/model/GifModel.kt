package com.knurenko.gifsearcher.domain.model

/**
 * @author Knurenko Bogdan 04.11.2025
 */
data class GifModel(
    val id: String,
    val url: String,
    val source: String,
    val title: String,
    val description: String,
    val rating: String
)
