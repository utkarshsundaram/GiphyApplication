package com.example.GiphyImageDisplay.datasource.datasourcefactory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.GiphyImageDisplay.datasource.networkstate.NetworkState
import com.example.GiphyImageDisplay.datasource.repository.GiphyRepository
import com.example.GiphyImageDisplay.model.GifFileResponse
import com.example.GiphyImageDisplay.utils.EventsUtils
import kotlinx.coroutines.*

class GifDataSource (private val repository: GiphyRepository,
                     private var limit: String,
                     private var offset: String,
                     private val scope: CoroutineScope): PageKeyedDataSource<Int, GifFileResponse>()  {


    private var supervisorJob = SupervisorJob()
    private val networkState = MutableLiveData<NetworkState>()
    private var retryQuery: (() -> Any)? = null // Keep reference of the last query (to be able to retry it if necessary)

    // OVERRIDE ---
    override fun loadInitial(params: PageKeyedDataSource.LoadInitialParams<Int>, callback: PageKeyedDataSource.LoadInitialCallback<Int, GifFileResponse>) {
        retryQuery = { loadInitial(params, callback) }
        executeQuery(1, params.requestedLoadSize) {
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, GifFileResponse>) {
        val page = params.key
        Log.d("pageNo",""+page)
        retryQuery = { loadAfter(params, callback) }
        executeQueryAfter(page, params.requestedLoadSize) {
            callback.onResult(it, page + 1)
        }
    }

    override fun loadBefore(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, GifFileResponse>) { }

    // UTILS ---
    private fun executeQuery(page: Int, perPage: Int, callback:(MutableList<GifFileResponse>) -> Unit) {
        networkState.postValue(NetworkState.RUNNING)
        scope.launch(getJobErrorHandler() + supervisorJob) {
            // To handle user typing case
            val users:MutableList<GifFileResponse> = repository.searchGIFsWithPagination(limit, offset.toString(),"g") as MutableList<GifFileResponse>
            retryQuery = null
            networkState.postValue(NetworkState.SUCCESS)
            callback(users)
        }
    }

    private fun executeQueryAfter(page: Int, perPage: Int, callback:(MutableList<GifFileResponse>) -> Unit) {
        networkState.postValue(NetworkState.RUNNING)
        scope.launch(getJobErrorHandler() + supervisorJob) {
            delay(5000) // To handle user typing case
            var offsetlimit=26*EventsUtils.offsetValue;
            EventsUtils.offsetValue++
            val users:MutableList<GifFileResponse> = repository.searchGIFsWithPagination(limit, offsetlimit.toString(),"g") as MutableList<GifFileResponse>
            retryQuery = null
            networkState.postValue(NetworkState.SUCCESS)
            callback(users)
        }
    }


    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        Log.e(GifDataSource::class.java.simpleName, "An error happened: $e")
        networkState.postValue(NetworkState.FAILED)
    }

    override fun invalidate() {
        super.invalidate()
        supervisorJob.cancelChildren()   // Cancel possible running job to only keep last result searched by user
    }

    // PUBLIC API ---

    fun getNetworkState(): LiveData<NetworkState> =

        networkState

    fun refresh() =
        this.invalidate()

    fun retryFailedQuery() {
        val prevQuery = retryQuery
        retryQuery = null
        prevQuery?.invoke()
    }
}


