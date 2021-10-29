package org.weyoung.stockcaculator

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.weyoung.stockcaculator.data.model.StockData
import org.weyoung.stockcaculator.data.remote.webPageUrl
import org.weyoung.stockcaculator.data.remote.wholePageUrl
import javax.inject.Inject

data class StockState(
    val isRefreshing: Boolean = true,
    val url: String = webPageUrl,
    val stockList: List<Stock> = emptyList()
)

data class Stock(val name: String, val code: String, val price: String, val limit: String)

@HiltViewModel
class StockViewModel @Inject constructor() : ViewModel() {
    private val _stockState = MutableStateFlow(StockState())
    val stockFlow = _stockState.asStateFlow()

    private var stockState: StockState
        get() = stockFlow.value
        set(value) {
            _stockState.value = value
        }

    fun refresh() {
        stockState = stockState.copy(isRefreshing = true, url = webPageUrl)
    }

    fun updateUrl(token: String) {
        stockState = stockState.copy(url = wholePageUrl(token = token))
    }

    fun loadStockList(data: StockData) {
        stockState = stockState.copy(
            isRefreshing = false,
            stockList = data.result.map {
                Stock(
                    it[1] as String,
                    it[0] as String,
                    it[2] as String,
                    it[3] as String
                )
            }
        )
    }
}