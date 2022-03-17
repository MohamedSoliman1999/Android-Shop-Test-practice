package com.example.androidshoptest.ui.cartlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidshoptest.db.CartDao
import com.example.androidshoptest.model.entity.CartItem
import com.example.androidshoptest.repository.cart.CartRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CartListViewModel @Inject constructor(
    private val cartDao:CartDao,
    private val cartRepo:CartRepositoryImpl
): ViewModel(){
    val cartItems=cartDao.observeAllShoppingItems()
    val cartTotalPrice=cartDao.observeTotalPrice()
    fun deleteCartItem(cartItem: CartItem){
        viewModelScope.launch {
            cartRepo.deleteShoppingItem(cartItem)
        }
    }
    fun insertCartItem(cartItem: CartItem){
        viewModelScope.launch {
            cartRepo.insertShoppingItem(cartItem)
        }
    }
}