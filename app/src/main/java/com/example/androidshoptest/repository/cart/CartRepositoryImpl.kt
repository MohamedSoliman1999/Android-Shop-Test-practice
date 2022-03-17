package com.example.androidshoptest.repository.cart

import androidx.lifecycle.LiveData
import com.example.androidshoptest.db.CartDao
import com.example.androidshoptest.model.entity.CartItem
import javax.inject.Inject

class CartRepositoryImpl@Inject constructor(
    private val shoppingDao: CartDao
):CartRepository {
    override suspend fun insertShoppingItem(shoppingItem: CartItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: CartItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItem(): LiveData<List<CartItem>> {
       return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }
}