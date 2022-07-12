package com.perpetio.well_be.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.perpetio.well_be.R
import com.perpetio.well_be.databinding.ItemLeftMessageBinding
import com.perpetio.well_be.databinding.ItemRightMessageBinding
import com.perpetio.well_be.dto.message.MessageModel
import com.perpetio.well_be.utils.ViewUtils.adjustTimePattern


class ChatAdapter(private val myProfileId: Long) :
    ListAdapter<MessageModel, RecyclerView.ViewHolder>(DiffCallbackMessage()) {

    companion object {
        const val LEFT_MSG_TYPE = 0
        const val RIGHT_MSG_TYPE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).let { messageModel ->
            if (messageModel.userId == myProfileId) RIGHT_MSG_TYPE else LEFT_MSG_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LEFT_MSG_TYPE -> LayoutInflater.from(parent.context)
                .let { ItemLeftMessageBinding.inflate(it, parent, false) }
                .let(::LeftMsgHolder)
            RIGHT_MSG_TYPE -> LayoutInflater.from(parent.context)
                .let { ItemRightMessageBinding.inflate(it, parent, false) }
                .let(::RightMsgHolder)
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == LEFT_MSG_TYPE) {
            (holder as LeftMsgHolder).bind(getItem(position))
        } else (holder as RightMsgHolder).bind(getItem(position))
    }

    override fun getItemCount(): Int = currentList.size

    inner class LeftMsgHolder(private val binding: ItemLeftMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: MessageModel) = with(binding) {
            tvUsername.text = message.user.username
            tvTime.text = message.createdTime.adjustTimePattern()
            tvMessageContent.text = message.text
            Glide.with(ivAvatar).load(message.user.avatarModel?.avatarUrl).error(R.drawable.avatar_static)
                .into(ivAvatar)
        }
    }

    inner class RightMsgHolder(private val binding: ItemRightMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: MessageModel) = with(binding) {
            tvUsername.text = message.user.username
            tvTime.text = message.createdTime.adjustTimePattern()
            tvMessageContent.text = message.text
            Glide.with(ivAvatar).load(message.user.avatarModel?.avatarUrl).error(R.drawable.avatar_static)
                .into(ivAvatar)
        }
    }
}

class DiffCallbackMessage : DiffUtil.ItemCallback<MessageModel>() {

    override fun areItemsTheSame(
        oldItem: MessageModel,
        newItem: MessageModel
    ): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: MessageModel,
        newItem: MessageModel
    ): Boolean =
        oldItem == newItem
}