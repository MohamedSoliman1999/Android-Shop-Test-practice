package com.example.androidshoptest.repository.cart

import androidx.lifecycle.LiveData
import com.example.androidshoptest.model.entity.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun insertShoppingItem(shoppingItem: CartItem)

    suspend fun deleteShoppingItem(shoppingItem: CartItem)

    fun observeAllShoppingItem(): Flow<List<CartItem>>

    fun observeTotalPrice(): Flow<Float>
}