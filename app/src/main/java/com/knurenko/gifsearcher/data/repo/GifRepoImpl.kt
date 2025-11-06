package com.knurenko.gifsearcher.data.repo

import com.knurenko.gifsearcher.data.mapper.GifMapper
import com.knurenko.gifsearcher.data.network_checker.NetworkChecker
import com.knurenko.gifsearcher.data.service.GifApiService
import com.knurenko.gifsearcher.domain.repo.GifRepo
import com.knurenko.gifsearcher.domain.repo.GifRepo.FetchException
import com.knurenko.gifsearcher.domain.repo.GifRepo.FetchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author Knurenko Bogdan 06.11.2025
 */
class GifRepoImpl(
    private val service: GifApiService,
    private val networkChecker: NetworkChecker,
    private val mapper: GifMapper
) : GifRepo {
    override suspend fun fetchGifs(
        searchQuery: String?,
        offset: Int
    ): FetchResult {

        val isNetworkAvailable = withContext(Dispatchers.IO) {
            networkChecker.isNetworkAvailable()
        }

        if (!isNetworkAvailable) return FetchResult.Failure(FetchException.NoInternet())

        try {
            val response = withContext(Dispatchers.IO) {
                service.fetchGifs(
                    searchQuery = searchQuery,
                    offset = offset
                )
            }

            if (response.meta.status != 200) {
                return FetchResult.Failure(
                    FetchException.ServerError(
                        code = response.meta.status,
                        message = response.meta.msg
                    )
                )
            } else {
                // success
                val items =
                    withContext(Dispatchers.Default) {
                        response.data.map { mapper.mapDtoToModel(it) }
                    }

                return FetchResult.Success(data = items)
            }
        } catch (e: Exception) {
            return FetchResult.Failure(FetchException.Unexpected(e))
        }
    }
}