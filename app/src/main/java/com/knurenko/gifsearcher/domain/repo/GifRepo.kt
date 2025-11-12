package com.knurenko.gifsearcher.domain.repo

import com.knurenko.gifsearcher.domain.model.GifModel

/**
 * Basic repo to fetch gifs from api
 * @author Knurenko Bogdan 04.11.2025
 */
interface GifRepo {
    /**
     * interface to represent fetch possible cases
     */
    sealed interface FetchResult {
        data class Success(val data: List<GifModel>) : FetchResult
        data class Failure(val reason: FetchException) : FetchResult
    }

    /**
     * possible exceptions that can appear on fetch's failure case
     */
    sealed interface FetchException {
        class NoInternet : FetchException
        data class ServerError(val code: Int, val message: String) : FetchException
        class Unexpected(val reason: Throwable) : FetchException
    }

    /**
     * method to fetch gifs from api
     * @param searchQuery - string query to search gis, default is null (by default we fetch most popular gifs)
     * @param offset - offset for pagination
     */
    suspend fun fetchGifs(searchQuery: String? = null, offset: Int = 0) : FetchResult
}