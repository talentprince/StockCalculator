package org.weyoung.stockcalculator.data.model

data class StockData(val token: String?, val result: List<StockInfo>)

typealias StockInfo = List<Any>
