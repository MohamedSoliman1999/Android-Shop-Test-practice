package com.example.androidshoptest.ui.cartlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidshoptest.model.entity.CartItem
import com.example.androidshoptest.repository.cart.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CartListViewModel @Inject constructor(
    private val cartRepo: CartRepository
): ViewModel(){
    val cartItems=cartRepo.observeAllShoppingItem()
    val cartTotalPrice=cartRepo.observeTotalPrice()
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