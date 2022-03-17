package com.example.androidshoptest.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidshoptest.model.entity.CartItem

@Database(
    entities = [CartItem::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun shoppingDao(): CartDao
}