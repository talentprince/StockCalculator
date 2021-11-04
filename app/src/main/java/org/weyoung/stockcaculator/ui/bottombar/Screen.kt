package org.weyoung.stockcaculator.ui.bottombar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import org.weyoung.stockcaculator.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector = Icons.Filled.Home
) {
    object Home : Screen("home", R.string.home)
    object List : Screen("list", R.string.list, Icons.Filled.List)
    object Favorite : Screen("favorite", R.string.favorite, Icons.Filled.Favorite)
    object Detail : Screen("detail", R.string.detail)
}