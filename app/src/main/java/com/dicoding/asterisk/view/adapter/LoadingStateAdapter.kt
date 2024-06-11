package com.dicoding.asterisk.view.adapter

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asterisk.databinding.LoadingItemBinding

class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateAdapter.ViewHolder>() {
    class ViewHolder(private val binding: LoadingItemBinding, retry: () -> Unit) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnRetry.setOnClickListener { retry.invoke() }
        }
        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.tvError.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.btnRetry.isVisible = loadState is LoadState.Error
            binding.tvError.isVisible = loadState is LoadState.Error
        }
    }
    override fun onBindViewHolder(holder: LoadingStateAdapter.ViewHolder, loadState: LoadState) {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateAdapter.ViewHolder {
        TODO("Not yet implemented")
    }
}