package com.example.freshgrade.ui.main.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.freshgrade.R
import com.example.freshgrade.databinding.FragmentProfileBinding
import com.example.freshgrade.ui.util.ViewModelFactory

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
        view.findViewById<ImageView>(R.id.backImageView)?.setOnClickListener {
            findNavController().popBackStack()
        }

        showUserProfile()

        profileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressbar.visibility = View.VISIBLE
            } else {
                binding.progressbar.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showUserProfile() {
        profileViewModel.userResponse.observe(viewLifecycleOwner) { userResponse ->
            binding.showUserName.text = userResponse.username
            binding.showEmail.text = userResponse.email
            Log.i("userResponse", userResponse.toString())
        }
        profileViewModel.getUserData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}