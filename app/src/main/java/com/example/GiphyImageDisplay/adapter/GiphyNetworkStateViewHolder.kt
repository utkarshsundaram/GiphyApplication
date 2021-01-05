package com.example.GiphyImageDisplay.adapter

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.GiphyImageDisplay.R
import com.example.GiphyImageDisplay.datasource.networkstate.NetworkState

class GiphyNetworkStateViewHolder(parent: View): RecyclerView.ViewHolder(parent) {

    // PUBLIC API ---
    fun bindTo(networkState: NetworkState?, callback: GiphyAdapter.OnClickListener) {
        hideViews()
        setVisibleRightViews(networkState)
        itemView.findViewById<Button>(R.id.item_search_user_network_state_button).setOnClickListener { callback.onClickRetry() }
    }

    // UTILS ---
    private fun hideViews() {
        itemView.findViewById<Button>(R.id.item_search_user_network_state_button).visibility = View.GONE
        itemView.findViewById<TextView>(R.id.item_search_user_network_state_title).visibility = View.GONE
        itemView.findViewById<ProgressBar>(R.id.item_search_user_network_state_progress_bar).visibility = View.GONE
    }

    private fun setVisibleRightViews(networkState: NetworkState?) {
        when (networkState) {
            NetworkState.FAILED -> {
                itemView.findViewById<Button>(R.id.item_search_user_network_state_button).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.item_search_user_network_state_title).visibility = View.VISIBLE
            }
            NetworkState.RUNNING -> {
                itemView.findViewById<ProgressBar>(R.id.item_search_user_network_state_progress_bar).visibility = View.VISIBLE
            }
        }
    }
}