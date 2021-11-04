package org.weyoung.stockcaculator.ui.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.weyoung.stockcaculator.database.FavoriteDao
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val favoriteDao: FavoriteDao) : ViewModel() {
    val favoriteItemState = favoriteDao.getAll()

    fun unFavorite(code: String) {
        viewModelScope.launch {
            favoriteDao.delete(code)
        }
    }
}