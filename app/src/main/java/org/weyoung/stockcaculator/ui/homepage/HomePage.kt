package org.weyoung.stockcaculator.ui.homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.weyoung.stockcaculator.ui.HiddenWebpage
import org.weyoung.stockcaculator.ui.theme.StockCaculatorTheme

@Composable
fun HomePage() {
    Surface(color = MaterialTheme.colors.background) {
        val viewModel: HomePageViewModel = viewModel()
        val stockState = viewModel.stockFlow.collectAsState()
        Column(
            modifier = Modifier.background(
                color = MaterialTheme.colors.secondary.copy(
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
                onRefresh = { viewModel.refresh() }
            ) {
                StockList(
                    stockList = stockState.value.stockList
                )
            }
        }
    }
}

@Composable
fun StockList(stockList: List<Stock>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(stockList) { stockData ->
            StockLine(modifier = modifier, stockData = stockData)
        }
    }
}

@Composable
fun StockLine(stockData: Stock, modifier: Modifier = Modifier) {
    Card(modifier = modifier.padding(2.dp)) {
        val numberColor = if (stockData.limit.toFloat() < 0) Color.Green else Color.Red
        Row(
            modifier = modifier
                .padding(start = 4.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.weight(1f)
            ) {
                Text(text = stockData.name, fontSize = 24.sp)
                Text(text = stockData.code, fontSize = 14.sp, color = Color.Gray)
            }
            Text(
                text = stockData.price, fontSize = 22.sp, color = numberColor, modifier = modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            )
            Text(
                text = stockData.limit, fontSize = 24.sp, color = numberColor, modifier = modifier
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
        StockList(stockList = listOf(Stock("Apple", "10001", "999", "10")))
    }
}