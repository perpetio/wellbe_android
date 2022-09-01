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
import com.perpetio.well_be.databinding.FragmentMyPostsBinding
import com.perpetio.well_be.dto.post.PostModel
import com.perpetio.well_be.ui.main.BaseFragmentWithBinding
import com.perpetio.well_be.ui.main.adapter.MarginItemDecoration
import com.perpetio.well_be.ui.main.adapter.MyPostAdapter
import com.perpetio.well_be.ui.main.viewmodel.PostViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MyPostFragment : BaseFragmentWithBinding<FragmentMyPostsBinding>(), MyPostAdapter.PostListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMyPostsBinding
        get() = FragmentMyPostsBinding::inflate

    private val postViewModel: PostViewModel by sharedViewModel()
    private lateinit var postAdapter: MyPostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postAdapter = MyPostAdapter(postViewModel.userId, this)
        postViewModel.loadMyPosts()
        binding.rvPosts.apply {
            adapter = postAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(
                MarginItemDecoration(16)
            )
        }
        postViewModel.myPosts.observe(viewLifecycleOwner) { posts ->
            postAdapter.submitList(posts.map { it.copy() })
        }
        binding.swipeRefresh.setOnRefreshListener {
            postViewModel.loadMyPosts()
            binding.swipeRefresh.isRefreshing = false
        }
        lifecycleScope.launchWhenStarted {
            postViewModel.errorEvent.collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onRemovePost(post: PostModel) {
        postViewModel.removePost(post)
    }

    override fun onEditPost(post: PostModel) {
        findNavController().navigate(
            HomeFragmentDirections.actionNavigationHomeToEditPostFragment(
                post
            )
        )
    }

    override fun onOpenChat(post: PostModel) {
        findNavController().navigate(
            HomeFragmentDirections.actionNavigationHomeToNavigationChatMessage(
                post
            )
        )
    }

    override fun onLikePost(post: PostModel) {
        postViewModel.onLikePost(post)
    }

    override fun onDislikePost(post: PostModel) {
        postViewModel.onDislikePost(post)
    }
}