package com.example.GiphyImageDisplay.model

import com.google.gson.annotations.SerializedName

data class GifModel(@SerializedName("images")var images:GifFileResponse) {
}