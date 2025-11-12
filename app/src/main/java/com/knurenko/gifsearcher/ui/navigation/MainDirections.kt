package com.knurenko.gifsearcher.ui.navigation

import kotlinx.serialization.Serializable

/**
 * @author Knurenko Bogdan 12.11.2025
 */
interface MainDirections {
    @Serializable data object Main
    @Serializable data class GifInfo(val id: String)
}