package com.knurenko.gifsearcher.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.knurenko.gifsearcher.ui.screens.gif_info.GifInfoScreenContainer
import com.knurenko.gifsearcher.ui.screens.main.MainScreenContainer

/**
 * @author Knurenko Bogdan 12.11.2025
 */

val LocalNavigation =
    staticCompositionLocalOf<NavHostController> { error("nav host controller wasn't initialized") }

@Composable
fun NavRoot() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavigation provides navController) {

        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .statusBarsPadding(),
            navController = navController,
            startDestination = MainDirections.Main,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            composable<MainDirections.Main> {
                MainScreenContainer()
            }
            composable<MainDirections.GifInfo> {
                val info: MainDirections.GifInfo = it.toRoute()
                GifInfoScreenContainer(info.id)
            }
        }
    }
}