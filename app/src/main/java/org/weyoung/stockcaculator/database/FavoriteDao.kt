package org.weyoung.stockcaculator.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * from favorite_table")
    fun getAll(): Flow<List<FavoriteItem>>

    @Query("SELECT * from favorite_table where code = :code")
    suspend fun getByCode(code: String): FavoriteItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FavoriteItem)

    @Query("DELETE FROM favorite_table WHERE code = :code")
    suspend fun delete(code: String)
}