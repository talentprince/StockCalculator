package org.weyoung.stockcaculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import org.weyoung.stockcaculator.ui.bottombar.Screen
import org.weyoung.stockcaculator.ui.detailpage.DetailPage
import org.weyoung.stockcaculator.ui.homepage.HomePage
import org.weyoung.stockcaculator.ui.theme.StockCaculatorTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockCaculatorTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route
                ) {
                    composable(Screen.Home.route) {
                        HomePage(openDetail = {
                            navController.navigate(Screen.Detail.withCode(it))
                        })
                    }
                    composable(
                        Screen.Detail.route,
                        arguments = listOf(navArgument("code") { type = NavType.StringType })
                    ) { DetailPage(navController = navController, code = it.arguments?.getString("code")!!) }
                }
            }
        }
    }
}