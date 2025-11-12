package com.knurenko.gifsearcher.ui.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.knurenko.gifsearcher.domain.model.GifModel
import com.knurenko.gifsearcher.ui.navigation.LocalNavigation
import com.knurenko.gifsearcher.ui.navigation.MainDirections

/**
 * @author Knurenko Bogdan 12.11.2025
 */
@Composable
fun MainScreenContainer() {
    val navigator = LocalNavigation.current

    val onClickItem: (GifModel) -> Unit = remember {
        {
            navigator.navigate(MainDirections.GifInfo(it.id))
        }
    }

    val gifs: List<GifModel> = remember {
        List(6) {
            GifModel(
                id = "$it",
                url = "https://some.url$it.com",
                source = it.toString(),
                title = "gif #$it",
                description = "gif #$it desc",
                rating = "?"
            )
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items = gifs, key = { it.id }) {
            ItemRow(it, onClickItem)
        }
    }
}

@Composable
private fun ItemRow(item: GifModel, onClick: (GifModel) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick(item) }
            .padding(4.dp)
    ) {
        Text(item.title)
    }
}