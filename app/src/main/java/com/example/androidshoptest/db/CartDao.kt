package com.example.androidshoptest.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.androidshoptest.model.entity.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItem: CartItem)

    @Delete
    suspend fun deleteShoppingItem(shoppingItem: CartItem)

    @Query("SELECT * FROM shopping_items")
    fun observeAllShoppingItems(): Flow<List<CartItem>>

    @Query("SELECT SUM(price * amount) FROM shopping_items")
    fun observeTotalPrice(): Flow<Float>
}