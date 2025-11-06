package com.knurenko.gifsearcher.data.service

import com.knurenko.gifsearcher.data.dto.ApiResponse

/**
 * @author Knurenko Bogdan 06.11.2025
 */
interface GifApiService {
    suspend fun fetchGifs(searchQuery: String? = null, offset: Int = 0, totalSize: Int = 20): ApiResponse
}