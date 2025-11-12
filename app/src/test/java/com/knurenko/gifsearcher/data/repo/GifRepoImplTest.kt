package com.knurenko.gifsearcher.data.repo

import com.knurenko.gifsearcher.data.dto.ApiResponse
import com.knurenko.gifsearcher.data.dto.GifDto
import com.knurenko.gifsearcher.data.dto.Meta
import com.knurenko.gifsearcher.data.dto.Pagination
import com.knurenko.gifsearcher.data.mapper.GifMapper
import com.knurenko.gifsearcher.data.network_checker.NetworkChecker
import com.knurenko.gifsearcher.data.service.GifApiService
import com.knurenko.gifsearcher.domain.model.GifModel
import com.knurenko.gifsearcher.domain.repo.GifRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.UUID

/**
 * @author Knurenko Bogdan 06.11.2025
 */
class GifRepoImplTest {
    private lateinit var mockApiService: GifApiService
    private lateinit var mockNetworkChecker: NetworkChecker
    private lateinit var repo: GifRepoImpl

    private lateinit var mapper: GifMapper

    @Before
    fun setup() {
        mockApiService = mockk()
        mockNetworkChecker = mockk()
        mapper = GifMapper()
        repo = GifRepoImpl(mockApiService, mockNetworkChecker, mapper)
    }

    @Test
    fun `fetchGifs returns Success when API call succeeds`(): Unit = runTest {
        // Given
        val expectedDtoList = List(2) {
            GifDto(
                id = "$it",
                url = "http://url_${it}.com",
                source = "$it",
                title = "Title of $it gif",
                description = "Description of $it gif",
                rating = "rating"
            )
        }

        val expectedModels = List(2) {
            GifModel(
                id = "$it",
                url = "http://url_${it}.com",
                source = "$it",
                title = "Title of $it gif",
                description = "Description of $it gif",
                rating = "rating"
            )
        }

        coEvery { mockNetworkChecker.isNetworkAvailable() } returns true
        coEvery {
            mockApiService.fetchGifs(
                any(),
                any()
            )
        } returns expectedDtoList.toSuccessResponse()

        println("starting fetch")
        // When
        val result = repo.fetchGifs(searchQuery = "cats", offset = 0)

        println("result of fetch is $result")
        // Then
        assertTrue(result is GifRepo.FetchResult.Success)
        assertEquals(expectedModels, (result as GifRepo.FetchResult.Success).data)
    }

    @Test
    fun `fetchGifs returns NoInternet when network unavailable`() = runTest {
        // Given
        coEvery { mockNetworkChecker.isNetworkAvailable() } returns false

        // When
        val result = repo.fetchGifs()
        println("")

        // Then
        assertTrue(result is GifRepo.FetchResult.Failure)
        assertTrue((result as GifRepo.FetchResult.Failure).reason is GifRepo.FetchException.NoInternet)
    }

    @Test
    fun `fetchGifs returns ServerError on HTTP error`() = runTest {
        // Given
        coEvery { mockNetworkChecker.isNetworkAvailable() } returns true
        coEvery { mockApiService.fetchGifs(any(), any()) } returns ApiResponse(
            data = emptyList(),
            meta = Meta(status = 500, msg = "Internal Server Error", responseId = UUID.randomUUID().toString()),
            pagination = null
        )

        // When
        val result = repo.fetchGifs()

        // Then
        assertTrue(result is GifRepo.FetchResult.Failure)
        val error =
            (result as GifRepo.FetchResult.Failure).reason as GifRepo.FetchException.ServerError
        assertEquals(500, error.code)
        assertEquals("Internal Server Error", error.message)
    }

    @Test
    fun `fetchGifs uses correct offset for pagination`() = runTest {
        // Given
        coEvery { mockNetworkChecker.isNetworkAvailable() } returns true
        coEvery {
            mockApiService.fetchGifs(
                any(),
                any()
            )
        } returns listOf<GifDto>().toSuccessResponse()

        // When
        repo.fetchGifs(searchQuery = "dogs", offset = 20)

        // Then
        coVerify { mockApiService.fetchGifs("dogs", 20) }
    }

    private fun List<GifDto>.toSuccessResponse(): ApiResponse = ApiResponse(
        data = this,
        meta = Meta(status = 200, msg = "success", responseId = UUID.randomUUID().toString()),
        pagination = Pagination(totalCount = 200, count = this.size, offset = 0)
    )
}