package com.knurenko.gifsearcher.data.mapper

import com.knurenko.gifsearcher.data.dto.GifDto
import com.knurenko.gifsearcher.domain.model.GifModel

/**
 * @author Knurenko Bogdan 06.11.2025
 */
class GifMapper {
    fun mapDtoToModel(dto: GifDto): GifModel = dto.toModel()

    private fun GifDto.toModel() = GifModel(id, url, source, title, description, rating)
}