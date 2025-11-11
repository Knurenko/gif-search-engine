package com.knurenko.gifsearcher.data.repo

import com.knurenko.gifsearcher.BuildConfig
import com.knurenko.gifsearcher.data.mapper.GifMapper
import com.knurenko.gifsearcher.data.network_checker.NetworkChecker
import com.knurenko.gifsearcher.data.service.GifApiService
import com.knurenko.gifsearcher.data.service.KtorGifApiService
import com.knurenko.gifsearcher.domain.repo.GifRepo
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test

/**
 * @author Knurenko Bogdan 11.11.2025
 */
class RepoWithRealServiceTest {

    private lateinit var realApiService: GifApiService
    private lateinit var mockNetworkChecker: NetworkChecker
    private lateinit var repo: GifRepoImpl

    private lateinit var mapper: GifMapper

    @Before
    fun setup() {
        mockNetworkChecker = mockk()

        val client = HttpClient(engineFactory = OkHttp, block = {
            install(ContentNegotiation) {
                json(Json { isLenient = true; ignoreUnknownKeys = true })
            }
        })

        val baseUrl = "https://api.giphy.com/v1/gifs"

        val apiKey = BuildConfig.GIPHY_API_KEY

        realApiService = KtorGifApiService(client, baseUrl, apiKey)

        mapper = GifMapper()
        repo = GifRepoImpl(realApiService, mockNetworkChecker, mapper)
    }

    @Test
    fun `repo-actually-fetch-trends`() = runTest {
        coEvery { mockNetworkChecker.isNetworkAvailable() } returns true
        val res = repo.fetchGifs()
        println("repo fetch result is $res")
        assert(res is GifRepo.FetchResult.Success)
    }

    @Test
    fun `repo-actually-fetch-search`() = runTest {
        coEvery { mockNetworkChecker.isNetworkAvailable() } returns true
        val res = repo.fetchGifs(searchQuery = "frog")
        println("repo fetch result is $res")
        assert(res is GifRepo.FetchResult.Success)
    }

    @Test
    fun `repo-next-page-items-do-not-collide-for-search`() = runTest {
        coEvery { mockNetworkChecker.isNetworkAvailable() } returns true
        val firstResult = repo.fetchGifs(searchQuery = "frog")
        val firstItems = when (firstResult) {
            is GifRepo.FetchResult.Failure -> {
                println("ERROR!")
                println("reason: ${firstResult.reason}")
                val error = when (firstResult.reason) {
                    is GifRepo.FetchException.NoInternet -> AssertionError("no internet")
                    is GifRepo.FetchException.ServerError -> AssertionError(firstResult.reason.message)
                    is GifRepo.FetchException.Unexpected -> AssertionError(
                        "unexpected",
                        firstResult.reason.reason
                    )
                }
                throw error
            }

            is GifRepo.FetchResult.Success -> firstResult.data
        }

        println("items fetched successfully! total count = ${firstItems.size}")
        val firstIdSet = firstItems.map { it.id }.toSet()

        val secondResult = repo.fetchGifs(searchQuery = "frog", offset = firstItems.size)
        val secondItems = when (secondResult) {
            is GifRepo.FetchResult.Success -> secondResult.data
            else -> throw AssertionError()
        }

        secondItems.forEach {
            if (firstIdSet.contains(it.id)) {
                println("fist items list: $firstItems")
                println("second items list: $secondItems")
                throw AssertionError("id collision found! id = ${it.id}")
            }
        }

        assert(true)
    }

    @Test
    fun `repo-next-page-items-do-not-collide-for-trends`() = runTest {
        coEvery { mockNetworkChecker.isNetworkAvailable() } returns true
        val firstResult = repo.fetchGifs()
        val firstItems = when (firstResult) {
            is GifRepo.FetchResult.Failure -> {
                println("ERROR!")
                println("reason: ${firstResult.reason}")
                val error = when (firstResult.reason) {
                    is GifRepo.FetchException.NoInternet -> AssertionError("no internet")
                    is GifRepo.FetchException.ServerError -> AssertionError(firstResult.reason.message)
                    is GifRepo.FetchException.Unexpected -> AssertionError(
                        "unexpected",
                        firstResult.reason.reason
                    )
                }
                throw error
            }

            is GifRepo.FetchResult.Success -> firstResult.data
        }

        println("items fetched successfully! total count = ${firstItems.size}")
        val firstIdSet = firstItems.map { it.id }.toSet()

        val secondResult = repo.fetchGifs(searchQuery = "", offset = firstItems.size)
        val secondItems = when (secondResult) {
            is GifRepo.FetchResult.Success -> secondResult.data
            else -> throw AssertionError()
        }

        secondItems.forEach {
            if (firstIdSet.contains(it.id)) {
                println("fist items list: $firstItems")
                println("second items list: $secondItems")
                throw AssertionError("id collision found! id = ${it.id}")
            }
        }

        assert(true)
    }
}