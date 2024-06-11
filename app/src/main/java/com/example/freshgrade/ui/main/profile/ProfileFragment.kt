package com.example.freshgrade.ui.main.profile

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.freshgrade.R
import com.example.freshgrade.databinding.FragmentProfileBinding
import com.example.freshgrade.ui.main.profile.about.AboutFragment
import com.example.freshgrade.ui.main.profile.privacy.PrivacyFragment
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


    }


}