package com.perpetio.well_be.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.perpetio.well_be.R
import com.perpetio.well_be.databinding.FragmentLoginBinding
import com.perpetio.well_be.dto.user.UserLoginModel
import com.perpetio.well_be.ui.auth.viewmodel.AuthViewModel
import com.perpetio.well_be.utils.ViewUtils.showHidePassword
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvGoToRegistration.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
        val passwordEditText = binding.viewInputPassword.etPassword
        binding.viewInputPassword.ivRevealEye.setOnClickListener {
            passwordEditText.showHidePassword()
        }
        binding.btnLogin.setOnClickListener {
            val password = binding.viewInputPassword.etPassword.text.toString()
            val email = binding.viewInputEmail.etEmail.text.toString()
            if (password.isEmpty() || email.isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_fill_all_fields),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            authViewModel.login(UserLoginModel(email, password))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}