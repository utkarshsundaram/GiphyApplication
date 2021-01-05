package com.example.GiphyImageDisplay.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.GiphyImageDisplay.R
import com.example.GiphyImageDisplay.datasource.networkstate.NetworkState
import com.example.GiphyImageDisplay.model.GifFileResponse

class GiphyAdapter(private val callback: OnClickListener): PagedListAdapter<GifFileResponse, RecyclerView.ViewHolder>(
    diffCallback
) {

    // FOR DATA ---
    private var networkState: NetworkState? = null
    interface OnClickListener {
        fun onClickRetry()
        fun whenListIsUpdated(size: Int, networkState: NetworkState?)
    }

    // OVERRIDE ---
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_display_gif -> GiphyDataViewHolder(
                view
            )
            R.layout.item_dispaly_gif_network_state -> GiphyNetworkStateViewHolder(
                view
            )
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_display_gif -> (holder as GiphyDataViewHolder).bindTo(getItem(position))
            R.layout.item_dispaly_gif_network_state -> (holder as GiphyNetworkStateViewHolder).bindTo(networkState, callback)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_dispaly_gif_network_state
        } else {
            R.layout.item_display_gif
        }
    }

    override fun getItemCount(): Int {
        this.callback.whenListIsUpdated(super.getItemCount(), this.networkState)
        return super.getItemCount()
    }

    // UTILS ---
    private fun hasExtraRow() = networkState != null && networkState != NetworkState.SUCCESS

    // PUBLIC API ---
    fun updateNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<GifFileResponse>() {
            override fun areItemsTheSame(oldItem: GifFileResponse, newItem: GifFileResponse): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: GifFileResponse, newItem: GifFileResponse): Boolean = oldItem == newItem
        }
    }
}