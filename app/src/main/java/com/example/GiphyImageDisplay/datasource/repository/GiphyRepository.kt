package com.example.GiphyImageDisplay.datasource.repository

import com.example.GiphyImageDisplay.BuildConfig
import com.example.GiphyImageDisplay.Repository.Service.GifService
import com.example.GiphyImageDisplay.model.GifFileResponse

class GiphyRepository(private val service: GifService) {

    private suspend fun search(limit:String,offset :String,rating:String) = service.getGIFImage(limit,offset,rating,BuildConfig.CLIENT_ID).await()


    suspend fun searchGIFsWithPagination(limit:String,offset :String,rating:String): List<GifFileResponse> {
        if (offset.isEmpty()) return listOf()

        val users = mutableListOf<GifFileResponse>()
        val request = search(limit,offset,rating)
        request.data.forEach {
            users.add(it.images)
        }
        return users
    }
}