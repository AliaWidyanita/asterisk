package com.dicoding.asterisk.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asterisk.data.remote.RestaurantItem
import com.dicoding.asterisk.databinding.RestaurantItemBinding
import com.dicoding.asterisk.view.DetailActivity
import com.dicoding.asterisk.view.DetailActivity.Companion.KEY_DETAIL

class RestaurantAdapter : RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {
    private var restaurantList: List<RestaurantItem> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RestaurantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(private val binding: RestaurantItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(resto: RestaurantItem) {
            binding.tvNameRestaurant.text = resto.name
            binding.tvAddressRestaurant.text = resto.address
            Glide.with(binding.root.context)
                .load(resto.imageUrl)
                .into(binding.ivRestaurantPhoto)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra(KEY_DETAIL, resto)
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    fun submitList(newRestaurantList: List<RestaurantItem>) {
        restaurantList = newRestaurantList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = restaurantList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(restaurantList[position])
    }


}