package com.perpetio.well_be.ui.main.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.perpetio.well_be.R
import com.perpetio.well_be.databinding.ItemPostBinding
import com.perpetio.well_be.dto.post.PostModel
import com.perpetio.well_be.utils.Sign
import com.perpetio.well_be.utils.ViewUtils.adjustTimePattern

class MyPostAdapter(private val myProfileId: Long, private val listener: PostListener) :
    ListAdapter<PostModel, MyPostAdapter.PostViewHolder>(DiffCallbackPost()) {

    interface PostListener {
        fun onRemovePost(post: PostModel)
        fun onEditPost(post: PostModel)
        fun onOpenChat(post: PostModel)
        fun onLikePost(post: PostModel)
        fun onDislikePost(post: PostModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        myProfileId
        holder.bind(getItem(position))
    }

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: PostModel) = with(binding) {
            ibRemove.visibility = View.VISIBLE
            tvAuthorName.text = post.user?.username ?: "admin"
            tvTitle.text = post.title
            tvContent.text = post.text
            tvTag.text = post.tag
            try {
                container.setCardBackgroundColor(Color.parseColor(post.backColor))
            } catch (ex: Exception) {
                container.setCardBackgroundColor(Color.parseColor("#81D4FA"))
            }
            if (post.userStatus.liked) ivLike.setImageResource(R.drawable.ic_favorite) else ivLike.setImageResource(
                R.drawable.ic_like
            )
            Glide.with(ivProfileIcon).load(post.user?.avatarUrl).error(R.drawable.avatar_static)
                .into(binding.ivProfileIcon)
            container.setOnClickListener {
                listener.onEditPost(post)
            }
            ivComment.setOnClickListener {
                listener.onOpenChat(post)
            }
            ibRemove.setOnClickListener {
                listener.onRemovePost(post)
            }
            ivLike.setOnClickListener {
                if (post.userStatus.liked) {
                    listener.onDislikePost(post)
                } else listener.onLikePost(post)
            }
            tvCreatedDate.text = post.createdTime?.adjustTimePattern()
            if (post.sign != 0) {
                binding.ivSign.visibility = View.VISIBLE
                ivSign.visibility = View.VISIBLE
                ivSign.setColorFilter(Color.parseColor(post.signColor))
                Glide.with(ivSign).load(Sign.getSignDrawableByKey(post.sign))
                    .into(ivSign)
            } else binding.ivSign.visibility = View.GONE
        }
    }
}

class DiffCallbackPost : DiffUtil.ItemCallback<PostModel>() {

    override fun areItemsTheSame(
        oldItem: PostModel,
        newItem: PostModel
    ): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: PostModel,
        newItem: PostModel
    ): Boolean =
        oldItem == newItem
}