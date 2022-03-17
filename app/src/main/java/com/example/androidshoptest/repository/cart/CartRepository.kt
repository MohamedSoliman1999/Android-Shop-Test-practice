package com.example.androidshoptest.repository.cart

import androidx.lifecycle.LiveData
import com.example.androidshoptest.model.entity.CartItem

interface CartRepository {
    suspend fun insertShoppingItem(shoppingItem: CartItem)

    suspend fun deleteShoppingItem(shoppingItem: CartItem)

    fun observeAllShoppingItem(): LiveData<List<CartItem>>

    fun observeTotalPrice(): LiveData<Float>
}