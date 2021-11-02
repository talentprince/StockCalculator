package org.weyoung.stockcaculator.ui.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.weyoung.stockcaculator.data.model.StockData
import org.weyoung.stockcaculator.data.remote.webPageUrl
import org.weyoung.stockcaculator.data.remote.wholePageUrl
import org.weyoung.stockcaculator.database.StockDao
import org.weyoung.stockcaculator.database.StockItem
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

data class StockState(
    val isRefreshing: Boolean = true,
    val url: String = webPageUrl,
    val stockList: List<StockItem> = emptyList()
)

@HiltViewModel
class HomePageViewModel @Inject constructor(val dao: StockDao) : ViewModel() {
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
        viewModelScope.launch {
            stockState = stockState.copy(
                isRefreshing = false,
                stockList = data.result.map {
                    StockItem(
                        it[0] as String,
                        it[1] as String,
                        it[2] as String,
                        it[3] as String,
                        (it[8] as Double).toString(),
                        SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(Date())
                    ).also { item ->
                        dao.insert(item)
                    }

                }
            )
        }
    }
}