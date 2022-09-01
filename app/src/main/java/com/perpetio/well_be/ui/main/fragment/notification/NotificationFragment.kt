package com.perpetio.well_be.ui.main.fragment.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.perpetio.well_be.R
import com.perpetio.well_be.databinding.FragmentNotificationBinding
import com.perpetio.well_be.ui.main.BaseFragmentWithBinding

class NotificationFragment : BaseFragmentWithBinding<FragmentNotificationBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNotificationBinding
        get() = FragmentNotificationBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(requireContext(), getString(R.string.available_soon), Toast.LENGTH_LONG)
            .show()
        findNavController().navigate(R.id.navigation_home)
    }

}