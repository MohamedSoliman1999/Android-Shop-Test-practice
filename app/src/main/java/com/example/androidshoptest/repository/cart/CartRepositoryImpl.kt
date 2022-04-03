package com.example.androidshoptest.repository.cart

import androidx.lifecycle.LiveData
import com.example.androidshoptest.db.CartDao
import com.example.androidshoptest.model.entity.CartItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

open class CartRepositoryImpl@Inject constructor(
    private val shoppingDao: CartDao
):CartRepository {
    override suspend fun insertShoppingItem(shoppingItem: CartItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: CartItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItem(): Flow<List<CartItem>> {
       return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): Flow<Float> {
        return shoppingDao.observeTotalPrice()
    }
}