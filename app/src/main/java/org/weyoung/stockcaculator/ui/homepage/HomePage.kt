package org.weyoung.stockcaculator.ui.homepage

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.weyoung.stockcaculator.ui.bottombar.Screen

@Composable
fun HomePage(modifier: Modifier = Modifier, openDetail: (String) -> Unit) {
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
            composable(Screen.List.route) { StockView(openDetail = openDetail) }
            composable(Screen.Favorite.route) { FavoriteView(openDetail = openDetail) }
        }
    }
}
