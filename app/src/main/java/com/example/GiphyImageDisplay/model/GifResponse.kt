package com.example.GiphyImageDisplay.model

import com.google.gson.annotations.SerializedName

data class GifResponse(@SerializedName("data")val data:ArrayList<GifModel>) {
}