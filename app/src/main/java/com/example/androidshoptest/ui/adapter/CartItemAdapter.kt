package com.example.androidshoptest.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidshoptest.R
import com.example.androidshoptest.databinding.CartItemBinding
import com.example.androidshoptest.model.entity.CartItem

class CartItemAdapter: ListAdapter<CartItem, CartItemAdapter.CartViewHolder>(CartItemDiffUtil){
    inner class CartViewHolder(private val binding:CartItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.cartItem=currentList[position]
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val item:CartItemBinding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.cart_item,
            parent,
            false
        )
        return CartViewHolder(item)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }
}
object CartItemDiffUtil: DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem.id==newItem.id
    }

}