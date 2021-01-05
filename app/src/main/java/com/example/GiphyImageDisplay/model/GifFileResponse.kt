package com.example.GiphyImageDisplay.model

import com.google.gson.annotations.SerializedName

data class GifFileResponse(@SerializedName("original")var original: GifFile?,
                           @SerializedName("downsized")var downsized: GifFile?) {
}