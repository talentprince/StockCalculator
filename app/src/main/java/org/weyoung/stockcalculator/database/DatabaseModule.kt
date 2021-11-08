package org.weyoung.stockcalculator.database

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideStockDao(@ApplicationContext context: Context): StockDao = StockDatabase.getInstance(context).stockDao()

    @Provides
    @Singleton
    fun provideFavoriteDao(@ApplicationContext context: Context): FavoriteDao = StockDatabase.getInstance(context).favoriteDao()
}