package com.example.androidshoptest.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidshoptest.R
import com.example.androidshoptest.databinding.GalleryItemBinding

class GalleryAdapter(private val galleryReactor:GalleryReactors):ListAdapter<String, GalleryAdapter.GalleryViewHolder>(GalleryDIffUtil) {
    inner class GalleryViewHolder(private val binding:GalleryItemBinding,galleryReactor:GalleryReactors):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.url=currentList[position]
            binding.galleryReactors=galleryReactor
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val item:GalleryItemBinding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.gallery_item,
            parent,
            false
        )
        return GalleryViewHolder(item,galleryReactor)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(position)
    }
}
object GalleryDIffUtil: DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
       return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem.equals(newItem,true)
    }

}
interface GalleryReactors{
    fun onGalleryItemClicked(url:String)
}