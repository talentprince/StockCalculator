package org.weyoung.stockcalculator.ui.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.weyoung.stockcalculator.data.model.StockData
import org.weyoung.stockcalculator.data.remote.webPageUrl
import org.weyoung.stockcalculator.data.remote.wholePageUrl
import org.weyoung.stockcalculator.database.FavoriteDao
import org.weyoung.stockcalculator.database.FavoriteItem
import org.weyoung.stockcalculator.database.StockDao
import org.weyoung.stockcalculator.database.StockItem
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class StockState(
    val isRefreshing: Boolean = true,
    val url: String = webPageUrl,
    val stockList: List<StockItem> = emptyList()
)

@HiltViewModel
class StockViewModel @Inject constructor(
    private val stockDao: StockDao,
    private val favoriteDao: FavoriteDao
) :
    ViewModel() {
    private val _stockState = MutableStateFlow(StockState())
    val stockFlow = _stockState.asStateFlow()
    private val _revealedState = MutableStateFlow(listOf<String>())
    val revealedFlow = _revealedState.asStateFlow()
    val favoriteFlow: Flow<List<String>> = favoriteDao.getAll().map { it.map { it.code } }

    private var stockState: StockState
        get() = stockFlow.value
        set(value) {
            _stockState.value = value
        }

    fun onExpanded(code: String) {
        if (_revealedState.value.contains(code)) return
        _revealedState.value = _revealedState.value.toMutableList().apply { add(code) }
    }

    fun onCollapsed(code: String) {
        if (!_revealedState.value.contains(code)) return
        _revealedState.value = _revealedState.value.toMutableList().apply { remove(code) }
    }

    fun refresh() {
        stockState = stockState.copy(isRefreshing = true, url = webPageUrl)
    }

    fun updateUrl(token: String) {
        stockState = stockState.copy(url = wholePageUrl(token = token))
    }

    fun favorite(item: FavoriteItem) {
        viewModelScope.launch {
            if (favoriteDao.getByCode(item.code) != null) {
                favoriteDao.delete(item.code)
            } else {
                favoriteDao.insert(item)
            }
        }
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
                        stockDao.insert(item)
                    }

                }
            )
        }
    }
}