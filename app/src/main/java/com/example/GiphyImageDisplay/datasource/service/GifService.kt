package com.example.GiphyImageDisplay.Repository.Service

import com.example.GiphyImageDisplay.model.GifResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface GifService {

    @GET("/v1/gifs/trending")
    fun getGIFImage(
                  @Query("limit") limit: String,
                  @Query("offset")offset:String,
                  @Query("rating") rating: String,
                 @Query("api_key") apiKey:String) : Deferred<GifResponse>
}