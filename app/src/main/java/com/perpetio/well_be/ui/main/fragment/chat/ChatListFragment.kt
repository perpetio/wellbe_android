package com.perpetio.well_be.ui.main.fragment.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.perpetio.well_be.databinding.FragmentChatListBinding
import com.perpetio.well_be.dto.post.PostModel
import com.perpetio.well_be.ui.main.adapter.ChatListAdapter
import com.perpetio.well_be.ui.main.adapter.MarginItemDecoration
import com.perpetio.well_be.ui.main.viewmodel.ChatViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ChatListFragment : Fragment(), ChatListAdapter.ChatListListener {

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!
    private val chatViewModel: ChatViewModel by sharedViewModel()
    private val chatListAdapter: ChatListAdapter by lazy {
        ChatListAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatViewModel.getAllChatsList()
        binding.rvChatList.apply {
            adapter = chatListAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(
                MarginItemDecoration(24)
            )
        }
        chatViewModel.chats.observe(viewLifecycleOwner) { chats ->
            chatListAdapter.submitList(chats.map { it.copy() })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOpenChat(post: PostModel) {
        findNavController().navigate(
            ChatListFragmentDirections.actionNavigationChatToNavigationChatMessage(
                post
            )
        )
    }
}