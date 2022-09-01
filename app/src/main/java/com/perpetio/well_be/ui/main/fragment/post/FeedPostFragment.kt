package com.perpetio.well_be.ui.main.fragment.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.perpetio.well_be.databinding.FragmentFeedPostsBinding
import com.perpetio.well_be.dto.post.PostModel
import com.perpetio.well_be.ui.main.BaseFragmentWithBinding
import com.perpetio.well_be.ui.main.adapter.MarginItemDecoration
import com.perpetio.well_be.ui.main.adapter.PostAdapter
import com.perpetio.well_be.ui.main.viewmodel.PostViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FeedPostFragment : BaseFragmentWithBinding<FragmentFeedPostsBinding>(), PostAdapter.FeedPostListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFeedPostsBinding
        get() = FragmentFeedPostsBinding::inflate

    private val postViewModel: PostViewModel by sharedViewModel()
    private lateinit var postAdapter: PostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postAdapter = PostAdapter(this)
        binding.rvPosts.apply {
            adapter = postAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(
                MarginItemDecoration(24)
            )
        }
        postViewModel.feedPosts.observe(viewLifecycleOwner) { posts ->
            postAdapter.submitList(posts.map { it.copy() })
        }
        binding.swipeRefresh.setOnRefreshListener {
            postViewModel.loadFeedPosts()
            binding.swipeRefresh.isRefreshing = false
        }
        lifecycleScope.launchWhenStarted {
            postViewModel.errorEvent.collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onOpenChat(post: PostModel) {
        findNavController().navigate(
            HomeFragmentDirections.actionNavigationHomeToNavigationChatMessage(post)
        )
    }

    override fun onLikePost(post: PostModel) {
        postViewModel.onLikePost(post)
    }

    override fun onDislikePost(post: PostModel) {
        postViewModel.onDislikePost(post)
    }

}