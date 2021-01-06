package com.example.GiphyImageDisplay.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.GiphyImageDisplay.R
import com.example.GiphyImageDisplay.model.GifFileResponse

class GiphyDataViewHolder(parent: View): RecyclerView.ViewHolder(parent) {

    // PUBLIC API ---
    fun bindTo(user: GifFileResponse?){
        user?.let {
            loadImage(it.downsized?.url, itemView.findViewById(R.id.item_search_user_image_profile))

        }
    }

    // UTILS ---
    private fun loadImage(url: String?, imageView: ImageView) {

        Glide.with(itemView.context)
            .load(url)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            //.transition(DrawableTransitionOptions.withCrossFade())
                //. .apply(RequestOptions.circleCropTransform())
            .into(imageView)
    }
}