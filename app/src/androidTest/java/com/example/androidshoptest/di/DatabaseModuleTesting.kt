package com.example.androidshoptest.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.androidshoptest.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModuleTesting {
    @Provides
    @Named("test_db")
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

}