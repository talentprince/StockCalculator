package org.weyoung.stockcalculator.ui.homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.weyoung.stockcalculator.database.FavoriteItem
import org.weyoung.stockcalculator.ui.theme.StockCalculatorTheme

@Composable
fun FavoriteView(modifier: Modifier = Modifier, openDetail: (String) -> Unit) {
    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = modifier.background(
                color = MaterialTheme.colors.onPrimary.copy(
                    alpha = 0.3f
                )
            )
        ) {
            val viewModel: FavoriteViewModel = hiltViewModel()
            val favoriteItemState = viewModel.favoriteItemState.collectAsState(emptyList())
            LazyColumn {
                items(favoriteItemState.value) { item ->
                    FavoriteLine(
                        favoriteItem = item,
                        unFavorite = viewModel::unFavorite,
                        openDetail = openDetail
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoriteLine(
    modifier: Modifier = Modifier,
    favoriteItem: FavoriteItem,
    unFavorite: (String) -> Unit,
    openDetail: (String) -> Unit
) {
    Card(
        modifier = modifier
            .clickable { openDetail(favoriteItem.code) }
            .padding(vertical = 2.dp, horizontal = 2.dp)
            .height(75.dp),
        elevation = 2.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { unFavorite(favoriteItem.code) }) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
            }
            Text(
                text = favoriteItem.name,
                style = MaterialTheme.typography.h5,
                modifier = modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            )
            Text(
                text = favoriteItem.code,
                style = MaterialTheme.typography.h5,
                modifier = modifier
                    .weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritePreview() {
    StockCalculatorTheme {
        FavoriteLine(
            favoriteItem = FavoriteItem("10001", "Apple"),
            unFavorite = {},
            openDetail = {}
        )
    }
}
