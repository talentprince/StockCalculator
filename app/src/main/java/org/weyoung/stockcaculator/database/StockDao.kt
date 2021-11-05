package org.weyoung.stockcaculator.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Query("SELECT * from stock_table where code = :code")
    fun getByCode(code: String): Flow<List<StockItem>>

    @Query("SELECT * from stock_table where code = :code limit 1")
    suspend fun first(code: String): StockItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: StockItem)

    @Update
    suspend fun update(item: StockItem)

    @Delete
    suspend fun delete(item: StockItem)
}