package org.weyoung.stockcalculator.ui.detailpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.weyoung.stockcalculator.database.FavoriteDao
import org.weyoung.stockcalculator.database.FavoriteItem
import org.weyoung.stockcalculator.database.StockDao
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val stockDao: StockDao,
    private val favoriteDao: FavoriteDao
) : ViewModel() {
    val favoriteState = favoriteDao.getAll().map { it.map { it.code } }

    fun stockRecords(code: String) = stockDao.getByCode(code)

    fun favorite(code: String) {
        viewModelScope.launch {
            if (favoriteDao.getByCode(code) != null) {
                favoriteDao.delete(code)
            } else {
                val stock = stockDao.first(code)
                stock?.let { favoriteDao.insert(FavoriteItem(it.code, it.name)) }
            }
        }
    }
}