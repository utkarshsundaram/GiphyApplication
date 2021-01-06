package com.example.GiphyImageDisplay.viemodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.GiphyImageDisplay.base.BaseViewModel
import com.example.GiphyImageDisplay.datasource.datasourcefactory.GifDataSourceFactory
import com.example.GiphyImageDisplay.datasource.networkstate.NetworkState
import com.example.GiphyImageDisplay.datasource.repository.GiphyRepository

class GiphyViewModel(repository: GiphyRepository): BaseViewModel() {

    // FOR DATA ---
    private val userDataSource = GifDataSourceFactory(repository = repository, scope = ioScope)

    // OBSERVABLES ---
    val users = LivePagedListBuilder(userDataSource, pagedListConfig()).build()
    val networkState: LiveData<NetworkState>? =
        Transformations.switchMap(userDataSource.source) { it.getNetworkState() }


    fun fetchNextSetData(limit: String, offset: String) {

        userDataSource.updateNextDataSet(limit, offset)
    }

    /**
     * Retry possible last paged request (ie: network issue)
     */
    fun refreshFailedRequest() =
        userDataSource.getSource()?.retryFailedQuery()

    /**
     * Refreshes all list after an issue
     */
    fun refreshAllList() =
        userDataSource.getSource()?.refresh()


    private fun pagedListConfig() = PagedList.Config.Builder()
        .setInitialLoadSizeHint(100)
        .setEnablePlaceholders(false)
        .setPageSize(100)
        .build()


}