package com.perpetio.well_be.ui.main.fragment.post

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.perpetio.well_be.R
import com.perpetio.well_be.databinding.FragmentCreationPostBinding
import com.perpetio.well_be.dto.post.CreateEditPostModel
import com.perpetio.well_be.ui.main.BaseFragmentWithBinding
import com.perpetio.well_be.ui.main.dialog.ColorCustomizationPickerDialog
import com.perpetio.well_be.ui.main.viewmodel.PostViewModel
import com.perpetio.well_be.utils.Sign
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CreationPostFragment : BaseFragmentWithBinding<FragmentCreationPostBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreationPostBinding
        get() = FragmentCreationPostBinding::inflate

    private val postViewModel: PostViewModel by sharedViewModel()
    private var pickedContainerColor: String = "#80D8FF"
    private var pickedSignColor: String = "#F4FF81"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.viewBackColor.tvColorPick.setOnClickListener {
            ColorCustomizationPickerDialog.show(parentFragmentManager) { color ->
                pickedContainerColor = "#$color"
                setPickedColor()
            }
        }
        binding.viewSignColor.tvSignColorPick.setOnClickListener {
            ColorCustomizationPickerDialog.show(parentFragmentManager) { color ->
                pickedSignColor = "#$color"
                setPickedColor()
            }
        }
        setPickedColor()
        binding.viewInputDescription.etDescription.doAfterTextChanged {
            binding.viewInputDescription.tvCurrentCounter.text = it.toString().length.toString()
        }
        binding.btnCreate.setOnClickListener {
            postViewModel.createPost(
                CreateEditPostModel(
                    id = null,
                    title = binding.viewInputTitle.etTitle.text.toString(),
                    text = binding.viewInputDescription.etDescription.text.toString(),
                    tag = binding.viewInputTag.etTag.text.toString(),
                    visible = binding.switchButton.isChecked,
                    backColor = pickedContainerColor,
                    likes = 0,
                    sign = Sign.getSignDrawableByKey(binding.viewSignType.chipGroup.checkedChipId),
                    signColor = pickedSignColor
                )
            )
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            postViewModel.postActionEvent.collect {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.post_has_created),
                    Toast.LENGTH_LONG
                ).show()
                findNavController().popBackStack()
            }
        }
        lifecycleScope.launchWhenStarted {
            postViewModel.errorEvent.collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setPickedColor() {
        binding.viewBackColor.viewPickedColor.setBackgroundColor(
            Color.parseColor(
                pickedContainerColor
            )
        )
        binding.viewSignColor.viewSignPickedColor.setBackgroundColor(
            Color.parseColor(
                pickedSignColor
            )
        )
    }
}