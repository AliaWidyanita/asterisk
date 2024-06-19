package com.dicoding.asterisk.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asterisk.data.remote.RestaurantReview
import com.dicoding.asterisk.databinding.RestaurantItemBinding
import com.dicoding.asterisk.view.DetailActivity

class MyReviewAdapter : RecyclerView.Adapter<MyReviewAdapter.ReviewViewHolder>() {
    private var reviews: List<RestaurantReview> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = RestaurantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    class ReviewViewHolder(private val binding: RestaurantItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: RestaurantReview) {
            binding.tvNameRestaurant.text = review.name
            binding.tvAddressRestaurant.text = review.address
            Glide.with(binding.root.context)
                .load(review.imageUrl)
                .into(binding.ivRestaurantPhoto)

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_RESTAURANT_ID, review.id)
                    putExtra(DetailActivity.EXTRA_IMAGE_URL, review.imageUrl)
                    putExtra(DetailActivity.EXTRA_SOURCE, "myReview")
                    putExtra(DetailActivity.EXTRA_RESTAURANT_NAME, review.name)
                    putExtra(DetailActivity.EXTRA_RESTAURANT_ADDRESS, review.address)
                    putExtra(DetailActivity.EXTRA_RESTAURANT_REVIEW, review.review)
                }

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        binding.root.context as Activity,
                        Pair(binding.ivRestaurantPhoto, "photo"),
                        Pair(binding.tvNameRestaurant, "name"),
                        Pair(binding.tvAddressRestaurant, "address")
                    )
                binding.root.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
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