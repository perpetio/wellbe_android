package com.perpetio.well_be.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.perpetio.well_be.R
import com.perpetio.well_be.databinding.FragmentRegistrationBinding
import com.perpetio.well_be.dto.user.UserRegistrationModel
import com.perpetio.well_be.ui.auth.viewmodel.AuthViewModel
import com.perpetio.well_be.utils.ViewUtils.showHidePassword
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        val passwordEditText = binding.viewInputPassword.etPassword
        binding.viewInputPassword.ivRevealEye.setOnClickListener {
            passwordEditText.showHidePassword()
        }
        binding.tvGoToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSignUp.setOnClickListener {
            val password = binding.viewInputPassword.etPassword.text.toString()
            val username = binding.viewInputUsername.etUsername.text.toString()
            val email = binding.viewInputEmail.etEmail.text.toString()
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_fill_all_fields),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            authViewModel.registration(UserRegistrationModel(email, username, password))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}