package org.weyoung.stockcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import org.weyoung.stockcalculator.ui.bottombar.Screen
import org.weyoung.stockcalculator.ui.detailpage.DetailPage
import org.weyoung.stockcalculator.ui.homepage.HomePage
import org.weyoung.stockcalculator.ui.theme.StockCalculatorTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockCalculatorTheme {
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