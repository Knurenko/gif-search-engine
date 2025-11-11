package com.knurenko.gifsearcher.data.service

import com.knurenko.gifsearcher.data.dto.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * @author Knurenko Bogdan 11.11.2025
 */
class KtorGifApiService(
    private val client: HttpClient,
    private val baseUrl: String,
    private val apiKey: String
) : GifApiService {
    override suspend fun fetchGifs(
        searchQuery: String?,
        offset: Int,
        totalSize: Int
    ): ApiResponse {
        val url = if (searchQuery.isNullOrEmpty()) "$baseUrl/trending" else "$baseUrl/search"
        val response = client.get(url) {
            parameter("api_key", apiKey)
            parameter("limit", totalSize)
            if (offset != 0) {
                parameter("offset", offset)
            }
            if (!searchQuery.isNullOrEmpty()) {
                parameter("q", searchQuery)
            }
        }

        return response.body<ApiResponse>()
    }
}