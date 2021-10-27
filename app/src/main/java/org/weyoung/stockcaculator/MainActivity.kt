package org.weyoung.stockcaculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import org.weyoung.stockcaculator.data.remote.webPageUrl
import org.weyoung.stockcaculator.data.remote.wholePageUrl
import org.weyoung.stockcaculator.ui.HiddenWebpage
import org.weyoung.stockcaculator.ui.theme.StockCaculatorTheme
import org.weyoung.stockcaculator.ui.webViewState

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockCaculatorTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val viewModel: StockViewModel = viewModel()
                    val stockState = viewModel.stockFlow.collectAsState()
                    Row(Modifier.fillMaxSize()) {
                        HiddenWebpage(
                            stockState.value.url,
                            viewModel::updateUrl,
                            viewModel::loadStockList
                        )
                        Greeting(stockState.value.stockList.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StockCaculatorTheme {
        Greeting("Android")
    }
}