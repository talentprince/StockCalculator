package org.weyoung.stockcaculator.database

import androidx.room.*

@Dao
interface StockDao {
    @Query("SELECT * from stock_table")
    suspend fun getAll(): List<StockItem>

    @Query("SELECT * from stock_table where code = :id")
    suspend fun getById(id: String): StockItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: StockItem)

    @Update
    suspend fun update(item: StockItem)

    @Delete
    suspend fun delete(item: StockItem)
}