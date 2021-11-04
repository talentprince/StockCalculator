package org.weyoung.stockcaculator.ui.homepage

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FavoriteView() {
    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier.background(
                color = MaterialTheme.colors.onPrimary.copy(
                    alpha = 0.3f
                )
            )
        ) {
            val viewModel: FavoriteViewModel = hiltViewModel()
            val favoriteItemState = viewModel.favoriteItemState.collectAsState(emptyList())
            LazyColumn {
                items(favoriteItemState.value) { stockItem ->
                    Card(
                        modifier = Modifier
                            .padding(vertical = 2.dp, horizontal = 2.dp)
                            .height(75.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { viewModel.unFavorite(stockItem.code) }) {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                            Text(
                                text = stockItem.name,
                                style = MaterialTheme.typography.h5,
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .weight(1f)
                            )
                            Text(
                                text = stockItem.code,
                                style = MaterialTheme.typography.h5,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}