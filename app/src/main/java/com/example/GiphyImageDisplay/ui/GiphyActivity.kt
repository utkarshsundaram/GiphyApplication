package com.example.GiphyImageDisplay.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.GiphyImageDisplay.R
import com.example.GiphyImageDisplay.adapter.GiphyAdapter
import com.example.GiphyImageDisplay.datasource.networkstate.NetworkState
import com.example.GiphyImageDisplay.utils.EventsUtils
import com.example.GiphyImageDisplay.viemodel.GiphyViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class GiphyActivity :AppCompatActivity(),GiphyAdapter.OnClickListener {
    private val viewModel: GiphyViewModel by viewModel<GiphyViewModel>()
    private lateinit var adapter:GiphyAdapter
    private lateinit var recyclerView:RecyclerView
    private lateinit var emptyListImage:ImageView
    private lateinit var emptyListTitle:TextView
    private lateinit var emptyListButton:Button
    private lateinit var progressBar:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView=findViewById(R.id.fragment_search_user_rv)
        emptyListImage=findViewById(R.id.fragment_search_user_empty_list_image)
        emptyListTitle=findViewById(R.id.fragment_search_user_empty_list_title)
        emptyListButton=findViewById(R.id.fragment_search_user_empty_list_button)
        progressBar=findViewById(R.id.fragment_search_user_progress)
    }

    override fun onResume() {
        super.onResume()
        configureRecyclerView()
        configureObservables()
        configureOnClick()
    }

    private fun configureOnClick() {
        emptyListButton.setOnClickListener { viewModel.refreshAllList() }
    }

    private fun configureRecyclerView(){
        adapter = GiphyAdapter(this)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.adapter = adapter
    }

    private fun configureObservables() {

        viewModel.networkState?.observe(this, Observer { adapter.updateNetworkState(it) })
        viewModel.users.observe(this, Observer { adapter.submitList(it) })
    }

    // UPDATE UI ----
    private fun updateUIWhenEmptyList(size: Int, networkState: NetworkState?) {
        emptyListImage.visibility = View.GONE
        emptyListButton.visibility = View.GONE
        emptyListTitle.visibility = View.GONE
        if (size == 0) {
            when (networkState) {
                NetworkState.SUCCESS -> {
                    Glide.with(this).load(R.drawable.ic_directions_run_black_24dp).into(emptyListImage)
                    emptyListTitle.text = getString(R.string.no_result_found)
                    emptyListImage.visibility = View.VISIBLE
                    emptyListTitle.visibility = View.VISIBLE
                    emptyListButton.visibility = View.GONE
                }
                NetworkState.FAILED -> {
                    Glide.with(this).load(R.drawable.ic_healing_black_24dp).into(emptyListImage)
                    emptyListTitle.text = getString(R.string.technical_error)
                    emptyListImage.visibility = View.VISIBLE
                    emptyListTitle.visibility = View.VISIBLE
                    emptyListButton.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateUIWhenLoading(size: Int, networkState: NetworkState?) {
        progressBar.visibility = if (size == 0 && networkState == NetworkState.RUNNING) View.VISIBLE else View.GONE
    }

    override fun onClickRetry() {
        viewModel.refreshFailedRequest()
    }

    override fun whenListIsUpdated(size: Int, networkState: NetworkState?) {
        updateUIWhenLoading(size, networkState)
        updateUIWhenEmptyList(size, networkState)
       /* var offset=(25*EventsUtils.offsetValue)+1
        if(EventsUtils.offsetValue<=10){
            viewModel.fetchNextSetData("25",offset.toString())

        }
        EventsUtils.offsetValue++;*/

    }
}