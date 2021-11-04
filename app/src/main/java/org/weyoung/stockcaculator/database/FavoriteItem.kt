package org.weyoung.stockcaculator.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.concurrent.Immutable

@Immutable
@Entity(tableName = "favorite_table")
data class FavoriteItem(
    @PrimaryKey val code: String,
    @ColumnInfo(name = "name") val name: String
)