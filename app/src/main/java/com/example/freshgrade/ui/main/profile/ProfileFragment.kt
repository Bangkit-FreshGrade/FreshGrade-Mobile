package com.example.freshgrade.ui.main.profile

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.freshgrade.R
import com.example.freshgrade.databinding.FragmentProfileBinding
import com.example.freshgrade.ui.authorization.AuthActivity
import com.example.freshgrade.ui.changepass.ChangePasswordFragment
import com.example.freshgrade.ui.main.profile.about.AboutFragment
import com.example.freshgrade.ui.main.profile.privacy.PrivacyFragment
import com.example.freshgrade.ui.util.ViewModelFactory
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutButton.setOnClickListener {
            profileViewModel.logout()
        }

        view.findViewById<Button>(R.id.btn_about)?.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_aboutFragment)
        }
        view.findViewById<Button>(R.id.btn_privacy)?.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_privacyFragment)
        }

        view.findViewById<Button>(R.id.changePasswordButton)?.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
        }

        view.findViewById<Button>(R.id.btn_delete_acc)?.setOnClickListener {
            deleteAccount()
        }

        showUserProfile()

    }

    private fun showUserProfile() {
        profileViewModel.getUser().observe(viewLifecycleOwner) { userProfile ->
            binding.showUserName.text = userProfile.name
//            binding.showEmail.text = userProfile.email
        }
    }

    private fun deleteAccount() {
        lifecycleScope.launch {
            profileViewModel.deleteAccount()
            Toast.makeText(context, "Account deleted successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }


}