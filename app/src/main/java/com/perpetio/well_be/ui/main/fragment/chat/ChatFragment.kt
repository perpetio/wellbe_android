package com.perpetio.well_be.ui.main.fragment.chat

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.perpetio.well_be.R
import com.perpetio.well_be.databinding.FragmentChatBinding
import com.perpetio.well_be.dto.post.PostModel
import com.perpetio.well_be.ui.main.BaseFragmentWithBinding
import com.perpetio.well_be.ui.main.adapter.ChatAdapter
import com.perpetio.well_be.ui.main.adapter.MarginItemDecoration
import com.perpetio.well_be.ui.main.viewmodel.ChatViewModel
import com.perpetio.well_be.utils.ViewUtils.toEditable
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ChatFragment : BaseFragmentWithBinding<FragmentChatBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChatBinding
        get() = FragmentChatBinding::inflate

    private val chatViewModel: ChatViewModel by sharedViewModel()
    private lateinit var chatAdapter: ChatAdapter
    private val currentRoom: PostModel by lazy {
        arguments?.let { ChatFragmentArgs.fromBundle(it).postModel }!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatViewModel.getAllMessagesForRoom(currentRoom.id!!)
        chatAdapter = ChatAdapter(chatViewModel.userId)
        binding.tvTitle.text = currentRoom.title
        binding.tvUsername.text =
            if (TextUtils.isEmpty(currentRoom.user?.username)) "admin" else currentRoom.user?.username
        Glide.with(binding.ivProfileIcon).load(currentRoom.user?.avatarUrl)
            .error(R.drawable.avatar_static)
            .into(binding.ivProfileIcon)
        setJoinRoomStatus()
        binding.rvMessages.apply {
            adapter = chatAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(
                MarginItemDecoration(36)
            )
        }
        chatViewModel.messages.observe(viewLifecycleOwner) { messages ->
            chatAdapter.submitList(messages.map { it.copy() }) {
                binding.rvMessages.scrollToPosition(chatAdapter.itemCount - 1)
            }
        }
        binding.ivSend.setOnClickListener {
            val message = binding.etMessageInput.text.toString()
            if (message.isNotBlank()) {
                chatViewModel.sendMessage(message)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            chatViewModel.sentMessageEvent.collect {
                binding.etMessageInput.text = "".toEditable()
            }
        }
        if (currentRoom.userStatus.member == false) {
            binding.tvJoin.setOnClickListener {
                chatViewModel.onJoinChat(currentRoom.id!!, false)
            }
        }
        lifecycleScope.launchWhenStarted {
            chatViewModel.errorEvent.collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
        lifecycleScope.launchWhenStarted {
            chatViewModel.joinLeftEvent.collect {
                currentRoom.userStatus.member = it
                setJoinRoomStatus()
            }
        }
        lifecycle.addObserver(chatViewModel)
        binding.ivBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setJoinRoomStatus() {
        if (currentRoom.userStatus.member == true) {
            binding.tvJoin.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        chatViewModel.initSession(currentRoom.id!!)
    }

    override fun onStop() {
        super.onStop()
        chatViewModel.closeSession()
    }
}