package com.knurenko.gifsearcher.di.modules

import com.knurenko.gifsearcher.data.mapper.GifMapper
import com.knurenko.gifsearcher.data.repo.GifRepoImpl
import com.knurenko.gifsearcher.domain.repo.GifRepo
import org.koin.dsl.module

/**
 * @author Knurenko Bogdan 12.11.2025
 */
val repoModule = module {
    single<GifRepo> { GifRepoImpl(service = get(), networkChecker = get(), mapper = GifMapper()) }
}