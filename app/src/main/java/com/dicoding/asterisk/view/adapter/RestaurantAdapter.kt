package com.dicoding.asterisk.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asterisk.data.remote.RestaurantItem
import com.dicoding.asterisk.databinding.RestaurantItemBinding
import com.dicoding.asterisk.view.DetailActivity
import com.dicoding.asterisk.view.DetailActivity.Companion.KEY_DETAIL

class RestaurantAdapter : PagingDataAdapter<RestaurantItem, RestaurantAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private val binding: RestaurantItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(resto: RestaurantItem) {
            binding.tvNameRestaurant.text = resto.name
            binding.tvAddressRestaurant.text = resto.address
            Glide.with(binding.root.context)
                .load(resto.urlToImage)
                .into(binding.ivRestaurantPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RestaurantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(user)
        }

//        holder.itemView.setOnClickListener {
//            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
//            intent.putExtra(KEY_DETAIL, user)
//            holder.itemView.context.startActivity(intent)
//        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RestaurantItem>() {
            override fun areItemsTheSame(oldItem: RestaurantItem, newItem: RestaurantItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: RestaurantItem, newItem: RestaurantItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}