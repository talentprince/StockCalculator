package org.weyoung.stockcaculator.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock_db")
data class StockItem(
    @PrimaryKey val code: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: String,
    @ColumnInfo(name = "limit") val limit: String,
    @ColumnInfo(name = "bidding") val bidding: String,
    @ColumnInfo(name = "date") val date: String,
)