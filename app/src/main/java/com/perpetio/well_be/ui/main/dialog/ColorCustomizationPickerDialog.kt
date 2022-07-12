package com.perpetio.well_be.ui.main.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.perpetio.well_be.R
import com.perpetio.well_be.databinding.DialogColorPickerBinding
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener


class ColorCustomizationPickerDialog : BottomSheetDialogFragment() {

    companion object {
        private val TAG: String = ColorCustomizationPickerDialog::class.java.simpleName
        fun show(
            fm: FragmentManager,
            callback: ((String) -> Unit)
        ) {
            ColorCustomizationPickerDialog().apply {
                this.callback = callback
            }.show(fm, TAG)
        }
    }

    private var binding: DialogColorPickerBinding? = null
    private lateinit var callback: (String) -> Unit

    override fun getTheme(): Int {
        return R.style.TransparentBottomSheetDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dialogBinding = DialogColorPickerBinding.inflate(inflater, container, false)
        binding = dialogBinding
        return dialogBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val colorPickerView = binding?.colorPickerView

        colorPickerView?.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            v.onTouchEvent(event)
            true
        }

        binding?.colorPickerView?.setColorListener(ColorEnvelopeListener { _, _ ->
        })
        binding?.btnPick?.setOnClickListener {
            colorPickerView?.colorEnvelope?.hexCode?.let { it1 ->
                callback.invoke(it1)
            }
            dismiss()
        }
        binding?.btnCancel?.setOnClickListener {
            dismiss()
        }
    }
}