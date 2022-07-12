package com.perpetio.well_be.ui.main.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.perpetio.well_be.R
import com.perpetio.well_be.databinding.FragmentProfileBinding
import com.perpetio.well_be.ui.auth.AuthActivity
import com.perpetio.well_be.ui.main.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.user.observe(viewLifecycleOwner) {
            binding.tvUsername.text = it?.username
            binding.tvEmail.text =
                if (!it?.mail.isNullOrEmpty()) it?.mail else getString(R.string.mail_is_not_added)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            profileViewModel.logoutEvent.collect {
                AuthActivity.launchLogout(requireContext())
            }
        }
        profileViewModel.avatar.observe(viewLifecycleOwner) {
            Glide.with(requireContext()).load(it.avatarUrl).error(R.drawable.avatar_static)
                .into(binding.ivProfileIconBig)
        }
        with(binding) {
            tvDarkMode.setOnClickListener {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.available_soon),
                    Toast.LENGTH_LONG
                ).show()
            }
            tvNotifications.setOnClickListener {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.available_soon),
                    Toast.LENGTH_LONG
                ).show()
            }
            tvLogOut.setOnClickListener {
                profileViewModel.logout()
            }
            tvPersonalInfo.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToEditProfileFragment())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}