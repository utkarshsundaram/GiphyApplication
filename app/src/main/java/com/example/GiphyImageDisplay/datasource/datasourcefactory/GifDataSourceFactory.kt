package com.example.GiphyImageDisplay.datasource.datasourcefactory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.GiphyImageDisplay.datasource.repository.GiphyRepository
import com.example.GiphyImageDisplay.model.GifFileResponse
import kotlinx.coroutines.CoroutineScope

class GifDataSourceFactory(private val repository: GiphyRepository,
                           private var limit: String="25",
                           private var offset: String="1",
                           private val scope: CoroutineScope
): DataSource.Factory<Int, GifFileResponse>() {
    val source = MutableLiveData<GifDataSource>()

    override fun create(): DataSource<Int, GifFileResponse> {
        val source = GifDataSource(repository, limit, offset, scope)
        this.source.postValue(source)
        return source
    }
    fun getSource() = source.value


    fun updateNextDataSet(limit: String, offset: String){
        this.limit = limit
        this.offset = offset
        getSource()?.refresh()
    }
}