package com.luisfagundes.history.data.di

import android.content.Context
import androidx.room.Room
import com.luisfagundes.history.data.database.HistoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {

    @Singleton
    @Provides
    fun provideHistoryDatabase(@ApplicationContext context: Context): HistoryDatabase {
        return Room.databaseBuilder(
            context,
            HistoryDatabase::class.java,
            "history_database"
        ).fallbackToDestructiveMigration(false).build()
    }

    @Singleton
    @Provides
    fun provideDeviceDao(database: HistoryDatabase) = database.deviceDao()
}
