package com.example.freshgrade.ui.changepass

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.freshgrade.R
import com.example.freshgrade.data.api.Result
import com.example.freshgrade.data.api.Result.Error
import com.example.freshgrade.databinding.FragmentChangePasswordBinding
import com.example.freshgrade.ui.authorization.register.RegisterFragment
import com.example.freshgrade.ui.util.ViewModelFactory
import kotlinx.coroutines.launch


@Suppress("SameParameterValue")
class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private val changePasswordViewModel: ChangePasswordViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(false)


        binding.changePasswordButton.setOnClickListener {
            val currentPassword = binding.currentPasswordEdit.text.toString()
            val newPassword = binding.newPasswordEdit.text.toString()
            val confirmPassword = binding.confirmPasswordEdit.text.toString()
            if (validateInputs(currentPassword, newPassword, confirmPassword)){
                lifecycleScope.launch {
                    changePassword(currentPassword, newPassword, confirmPassword)
                }

            }

        }

    }

    private fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {
        changePasswordViewModel.changePassword(currentPassword, newPassword, confirmPassword)
            .observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Error -> {
                        showDialog(RegisterFragment.ERROR, result.error)
                        Log.e(
                            "ChangePasswordFragment",
                            "Failed to change password: ${result.error}"
                        )
                        showLoading(false)
                    }

                    Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        showDialog(RegisterFragment.SUCCESS)
                        Log.d("ChangePasswordFragment", "Password changed successfully")
                        Log.println(Log.INFO, "ChangePasswordFragment", result.toString())
                    }
                }
            }


    }

    private fun showDialog(mode: String, message: String? = null) {
        val builder = AlertDialog.Builder(requireContext())
        if (mode == RegisterFragment.ERROR) {
            val displayMessage = when {
                message?.contains("401") == true -> getString(R.string.password_unauthorized_error)
                message?.contains("400") == true -> getString(R.string.password_bad_request_error)

                message != null -> message
                else -> getString(R.string.change_password_failed)
            }
            builder.setTitle("Error")
            builder.setMessage(displayMessage)
            builder.setPositiveButton(android.R.string.ok) { _, _ -> }
            builder.show()
        } else if (mode == RegisterFragment.SUCCESS) {
            Toast.makeText(
                requireContext(),
                getString(R.string.change_password_success),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validateInputs(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Boolean {

        if (currentPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return false
        }
        if (newPassword != confirmPassword) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}