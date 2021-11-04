package org.weyoung.stockcaculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.weyoung.stockcaculator.ui.bottombar.Screen
import org.weyoung.stockcaculator.ui.homepage.HomePage
import org.weyoung.stockcaculator.ui.theme.StockCaculatorTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockCaculatorTheme {
                val navigationController = rememberNavController()
                NavHost(
                    navController = navigationController,
                    startDestination = Screen.Home.route
                ) {
                    composable(Screen.Home.route) { HomePage() }
                    composable(Screen.Detail.route) {}
                }
            }
        }
    }
}