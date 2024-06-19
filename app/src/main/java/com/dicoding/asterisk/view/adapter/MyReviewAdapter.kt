package com.dicoding.asterisk.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asterisk.data.remote.RestaurantReview
import com.dicoding.asterisk.databinding.RestaurantItemBinding

class MyReviewAdapter(private val onClick: (RestaurantReview, String) -> Unit) : RecyclerView.Adapter<MyReviewAdapter.ReviewViewHolder>() {
    private var reviews: List<RestaurantReview> = listOf()

    class ReviewViewHolder(val binding: RestaurantItemBinding, val onClick: (RestaurantReview, String) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: RestaurantReview) {
            binding.tvNameRestaurant.text = review.name
            binding.tvAddressRestaurant.text = review.address
            Glide.with(binding.root.context).load(review.imageUrl).into(binding.ivRestaurantPhoto)
            binding.root.setOnClickListener {
                onClick(review, "myReview")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = RestaurantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int = reviews.size

    fun setReviews(reviews: List<RestaurantReview>) {
        this.reviews = reviews
        notifyDataSetChanged()
    }
}