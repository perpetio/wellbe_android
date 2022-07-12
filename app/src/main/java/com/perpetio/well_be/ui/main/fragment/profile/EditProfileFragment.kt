package com.perpetio.well_be.ui.main.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.perpetio.well_be.R
import com.perpetio.well_be.databinding.FragmentEditProfileBinding
import com.perpetio.well_be.ui.main.dialog.AddPictureChooserDialog
import com.perpetio.well_be.ui.main.viewmodel.ProfileViewModel
import com.perpetio.well_be.utils.ViewUtils.toEditable
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            profileViewModel.editProfileEvent.collect {
                if (it) {
                    findNavController().popBackStack()
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            profileViewModel.errorEvent.collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
        lifecycleScope.launch {
            profileViewModel.user.observe(viewLifecycleOwner) {
                binding.viewInputCity.etCity.text =
                    it?.city?.toEditable()
                binding.viewInputCountry.etCountry.text =
                    it?.country?.toEditable()
                binding.viewInputUsername.etUsername.text =
                    it?.username?.toEditable()
                binding.tvEmail.text =
                    if (!it?.mail.isNullOrEmpty()) it?.mail else getString(R.string.mail_is_not_added)
            }
        }
        profileViewModel.avatar.observe(viewLifecycleOwner) {
            Glide.with(requireContext()).load(it.avatarUrl).error(R.drawable.avatar_static)
                .into(binding.ivProfileIconBig)
        }
        with(binding) {
            ivBackBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            btnSave.setOnClickListener {
                profileViewModel.editUser(
                    binding.viewInputUsername.etUsername.text.toString(),
                    binding.viewInputCountry.etCountry.text.toString(),
                    binding.viewInputCity.etCity.text.toString()
                )
            }
            ivProfileIconBig.setOnClickListener {
                if (parentFragmentManager.findFragmentByTag(AddPictureChooserDialog.TAG) == null) {
                    AddPictureChooserDialog.show(
                        parentFragmentManager,
                        pictureChooserListener
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val pictureChooserListener =
        object : AddPictureChooserDialog.PictureChooserListener {
            override fun onUploadPhoto() {
                pickImageFromGallery()
            }
        }

    private val fileChooserContract =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            imageUri?.let {
                profileViewModel.updateAvatar(
                    requireContext().contentResolver.openInputStream(it)?.readBytes()!!
                )
            }
        }

    private fun pickImageFromGallery() {
        fileChooserContract.launch("image/*")
    }

}