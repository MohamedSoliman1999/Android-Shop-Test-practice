package com.example.androidshoptest.ui.gallery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidshoptest.mainstate.MainState
import com.example.androidshoptest.model.datatransfer.ImageResponse
import com.example.androidshoptest.model.entity.CartItem
import com.example.androidshoptest.repository.cart.CartRepositoryImpl
import com.example.androidshoptest.repository.gallery.GalleryRepositoryImpl
import com.example.androidshoptest.util.Constants
import com.example.androidshoptest.util.Events
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val galleryRepo: GalleryRepositoryImpl,
    private val cartRpo: CartRepositoryImpl
) : ViewModel() {
    val getShoppingItems = cartRpo.observeAllShoppingItem()
    val totalPrice = cartRpo.observeTotalPrice()


    val galleryIntent = Channel<GalleryIntent<String>>(Channel.UNLIMITED)

    private val _imageResponse = MutableStateFlow<MainState<ImageResponse>>(MainState.Idle())
    val imageResponse get() = _imageResponse
    val selectedImageUrl = MutableLiveData<String>()

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            galleryIntent.consumeAsFlow().collect {
                when (it) {
                    is GalleryIntent.SearchImage -> searchImage(it.data)
                }
            }
        }
    }

    private fun searchImage(query: String?) {
        if (query.isNullOrEmpty()) {
            return
        }
        Log.e("searchImage", "Loading")
        _imageResponse.value = MainState.Loading()
        viewModelScope.launch {
            _imageResponse.value = try {
                val data = galleryRepo.searchForImage(query)
//                    .stateIn(viewModelScope, SharingStarted.Lazily, null).value!!
                MainState.Success(data)
            } catch (e: Exception) {
                Log.e("searchImage", "Error ${e.message}")
                MainState.Error(e)
            }
        }

//        _images.value = Events(Resource.loading(null))
//        viewModelScope.launch {
//            val response = repository.searchForImage(query)
//            _images.value = Events(response)
//        }
    }

    private val _insertShoppingItem = MutableLiveData<Events<MainState<CartItem>>>()
    val insertShoppingItem : LiveData<Events<MainState<CartItem>>> =_insertShoppingItem
    fun validateShoppingItem(name: String, amount: String, price: String){
        if (name.isEmpty() || amount.isEmpty() || price.isEmpty()){
            _insertShoppingItem.postValue(Events(MainState.Error(Throwable("Please enter all information"),null)))
            return
        }

        if (name.length > Constants.MAX_ITEM_NAME_LENGTH){
            _insertShoppingItem.postValue(Events(MainState.Error(Throwable("Length of item name should be less than ${Constants.MAX_ITEM_NAME_LENGTH}"),null)))
            return
        }

        if (price.length > Constants.MAX_ITEM_PRICE_LENGTH){
            _insertShoppingItem.postValue(Events(MainState.Error(Throwable("Length of price  should be less than ${Constants.MAX_ITEM_PRICE_LENGTH}"),null)))
            return
        }

        if (price.length > Constants.MAX_ITEM_PRICE_LENGTH){
            _insertShoppingItem.postValue(Events(MainState.Error(Throwable("Length of price  should be less than ${Constants.MAX_ITEM_PRICE_LENGTH}"),null)))
            return
        }

        val amountString = try {
            amount.toInt()
        }catch (e: java.lang.Exception){
            _insertShoppingItem.postValue(Events(MainState.Error(Throwable("Pleas enter valid amount"),null)))
            return
        }

        val shoppingItem = CartItem(name,amountString,price.toFloat(),selectedImageUrl.value?:"")
        insertShoppingItemInDB(shoppingItem)
        selectedImageUrl.postValue("")
        _insertShoppingItem.postValue(Events(MainState.Success(shoppingItem)))
    }
    fun insertShoppingItemInDB(shoppingItem: CartItem) = viewModelScope.launch {
        cartRpo.insertShoppingItem(shoppingItem)
    }
}