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
import com.perpetio.well_be.databinding.FragmentEditPostBinding
import com.perpetio.well_be.dto.post.CreateEditPostModel
import com.perpetio.well_be.dto.post.PostModel
import com.perpetio.well_be.ui.main.BaseFragmentWithBinding
import com.perpetio.well_be.ui.main.dialog.ColorCustomizationPickerDialog
import com.perpetio.well_be.ui.main.viewmodel.PostViewModel
import com.perpetio.well_be.utils.Sign
import com.perpetio.well_be.utils.ViewUtils.toEditable
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EditPostFragment : BaseFragmentWithBinding<FragmentEditPostBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentEditPostBinding
        get() = FragmentEditPostBinding::inflate

    private val postViewModel: PostViewModel by sharedViewModel()
    private var pickedColor: String = "#81D4FA"
    private var pickedSignColor: String = "#F4FF81"

    private val currentPost: PostModel? by lazy {
        arguments?.let { EditPostFragmentArgs.fromBundle(it).post }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            postViewModel.errorEvent.collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
        with(binding) {
            viewInputDescription.etDescription.doAfterTextChanged {
                binding.viewInputDescription.tvCurrentCounter.text = it.toString().length.toString()
            }
            viewInputTitle.etTitle.text = currentPost?.title?.toEditable()
            viewInputDescription.etDescription.text = currentPost?.text?.toEditable()
            viewInputTag.etTag.text = currentPost?.tag?.toEditable()
            pickedColor = currentPost?.backColor ?: "#81D4FA"
            pickedSignColor = currentPost?.signColor ?: "#81D4FA"
            switchButton.isChecked = currentPost?.visible == true

            ivCancel.setOnClickListener {
                findNavController().popBackStack()
            }
            viewBackColor.tvColorPick.setOnClickListener {
                ColorCustomizationPickerDialog.show(parentFragmentManager) { color ->
                    pickedColor = "#$color"
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
            btnSave.setOnClickListener {
                postViewModel.editPost(
                    CreateEditPostModel(
                        id = currentPost?.id,
                        title = viewInputTitle.etTitle.text.toString(),
                        text = viewInputDescription.etDescription.text.toString(),
                        tag = viewInputTag.etTag.text.toString(),
                        visible = binding.switchButton.isChecked,
                        backColor = pickedColor,
                        likes = 0,
                        sign = if (binding.viewSignType.chipGroup.checkedChipId == View.NO_ID) currentPost?.sign else Sign.getSignKeyByChipId(
                            binding.viewSignType.chipGroup.checkedChipId
                        ),
                        signColor = pickedSignColor
                    )
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            postViewModel.postActionEvent.collect {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.post_has_changed),
                    Toast.LENGTH_LONG
                ).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun setPickedColor() {
        binding.viewBackColor.viewPickedColor.setBackgroundColor(Color.parseColor(pickedColor))
        binding.viewSignColor.viewSignPickedColor.setBackgroundColor(
            Color.parseColor(
                pickedSignColor
            )
        )
    }
}