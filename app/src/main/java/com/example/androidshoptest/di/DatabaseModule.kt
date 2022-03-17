package com.example.androidshoptest.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.androidshoptest.db.AppDatabase
import com.example.androidshoptest.db.CartDao
import com.example.androidshoptest.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideRoom(
        @ApplicationContext context: Context,
        roomCallback: RoomDatabase.Callback
    ): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .addCallback(roomCallback)
        .build()

    @Provides
    fun provideRoomCallback()=object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            //Initialize Database if no database attached to the App
            super.onCreate(db)
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            //Re-open Database if it has database attached to the App
            super.onOpen(db)
        }
    }

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): CartDao {
        return db.shoppingDao()
    }
}