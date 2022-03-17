package com.example.androidshoptest.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.api.load

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, url: String?) {
    url?.let {
        imageView.load(it) {
            crossfade(true)
        }
    }
}
@BindingAdapter("priceText")
fun priceText(tV:com.google.android.material.textview.MaterialTextView,obj:Any){
    tV.text = obj.toString()
}
@BindingAdapter("amountText")
fun amountText(tV:com.google.android.material.textview.MaterialTextView,obj:Any){
    tV.text = "${obj}X"
}