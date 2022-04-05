package com.example.androidshoptest.ui.cartlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidshoptest.model.entity.CartItem
import com.example.androidshoptest.repository.cart.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CartListViewModel @Inject constructor(
    private val cartRepo: CartRepository
): ViewModel(){
    val cartItems: Flow<List<CartItem>> =cartRepo.observeAllShoppingItem().distinctUntilChanged().filterNotNull()
    val cartTotalPrice:Flow<Float> =cartRepo.observeTotalPrice().distinctUntilChanged().filterNotNull()
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