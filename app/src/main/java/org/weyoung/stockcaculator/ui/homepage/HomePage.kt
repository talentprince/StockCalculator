package org.weyoung.stockcaculator.ui.homepage

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.weyoung.stockcaculator.database.StockItem
import org.weyoung.stockcaculator.ui.HiddenWebpage
import org.weyoung.stockcaculator.ui.bottombar.Screen
import org.weyoung.stockcaculator.ui.theme.StockCaculatorTheme
import kotlin.math.roundToInt

@Composable
fun HomePage() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.List,
        Screen.Favorite,
    )
    Scaffold(bottomBar = {
        BottomNavigation {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            items.forEach { screen ->
                BottomNavigationItem(
                    icon = { Icon(screen.icon, contentDescription = null) },
                    label = { Text(stringResource(screen.resourceId)) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }) {
        NavHost(navController = navController, startDestination = Screen.List.route) {
            composable(Screen.List.route) { StockView() }
            composable(Screen.Favorite.route) {}
        }
    }
}

@Composable
private fun StockView() {
    Surface(color = MaterialTheme.colors.background) {
        val viewModel: HomePageViewModel = hiltViewModel()
        val stockState = viewModel.stockFlow.collectAsState()
        val revealedState = viewModel.revealedFlow.collectAsState()
        val favoriteState = viewModel.favoriteFlow.collectAsState(emptyList())
        Column(
            modifier = Modifier.background(
                color = MaterialTheme.colors.onPrimary.copy(
                    alpha = 0.3f
                )
            )
        ) {
            HiddenWebpage(
                stockState.value.url,
                viewModel::updateUrl,
                viewModel::loadStockList
            )
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = stockState.value.isRefreshing),
                onRefresh = viewModel::refresh
            ) {
                StockList(
                    stockList = stockState.value.stockList,
                    onExpanded = viewModel::onExpanded,
                    onCollapsed = viewModel::onCollapsed,
                    isRevealed = revealedState.value::contains,
                    isFavorite = favoriteState.value::contains,
                    onFavoriteClick = viewModel::favorite
                )
            }
        }
    }
}

@Composable
private fun StockList(
    stockList: List<StockItem>,
    modifier: Modifier = Modifier,
    isRevealed: (String) -> Boolean,
    isFavorite: (String) -> Boolean,
    onExpanded: (String) -> Unit,
    onCollapsed: (String) -> Unit,
    onFavoriteClick: (String) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(stockList) { stockItem ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
            ) {
                Row(
                    modifier = modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val starColor =
                        if (isFavorite(stockItem.code)) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground
                    IconButton(
                        onClick = { onFavoriteClick(stockItem.code) }) {
                        Icon(Icons.Filled.Favorite, contentDescription = null, tint = starColor)
                    }
                }
                StockLine(
                    modifier = modifier,
                    stockItem = stockItem,
                    isRevealed = isRevealed(stockItem.code),
                    onExpanded = onExpanded,
                    onCollapsed = onCollapsed
                )
            }
        }
    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
private fun StockLine(
    stockItem: StockItem,
    modifier: Modifier = Modifier,
    isRevealed: Boolean,
    onExpanded: (String) -> Unit,
    onCollapsed: (String) -> Unit
) {
    val transitionState = remember {
        MutableTransitionState(isRevealed).apply {
            targetState = !isRevealed
        }
    }
    val transition = updateTransition(transitionState, "cardTransition")
    val offsetTransition by transition.animateFloat(
        label = "cardOffsetTransition",
        transitionSpec = { tween(durationMillis = 500) },
        targetValueByState = { if (isRevealed) 128.dp.value else 0f },
    )
    val cardElevation by transition.animateDp(
        label = "cardElevation",
        transitionSpec = { tween(durationMillis = 500) },
        targetValueByState = { if (isRevealed) 8.dp else 2.dp }
    )

    Card(modifier = modifier
        .padding(vertical = 2.dp, horizontal = 2.dp)
        .height(75.dp)
        .offset { IntOffset(offsetTransition.roundToInt(), 0) }
        .pointerInput(Unit) {
            detectHorizontalDragGestures { _, dragAmount ->
                when {
                    dragAmount >= 6 -> onExpanded(stockItem.code)
                    dragAmount < -6 -> onCollapsed(stockItem.code)
                }
            }
        }, elevation = cardElevation
    ) {
        val numberColor = if (stockItem.limit.toFloat() < 0) Color.Green else Color.Red
        Row(
            modifier = modifier
                .padding(start = 4.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.weight(1f)
            ) {
                Text(text = stockItem.name, style = MaterialTheme.typography.h5)
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = stockItem.code, style = MaterialTheme.typography.subtitle1)
                }
            }
            Text(
                text = stockItem.price,
                style = MaterialTheme.typography.h5,
                color = numberColor,
                modifier = modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            )
            Text(
                text = stockItem.limit,
                style = MaterialTheme.typography.h5,
                color = numberColor,
                modifier = modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            )
            Text(
                text = stockItem.bidding,
                style = MaterialTheme.typography.h5,
                color = numberColor,
                modifier = modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StockCaculatorTheme {
        StockList(
            stockList = listOf(StockItem("Apple", "10001", "999", "10", "10", "2021-11-02")),
            onExpanded = {},
            onCollapsed = {},
            isRevealed = { true },
            onFavoriteClick = {},
            isFavorite = { true })
    }
}