package com.example.freshgrade.ui.authorization.landing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.freshgrade.R
import com.example.freshgrade.databinding.FragmentLandingBinding


class LandingFragment : Fragment() {
    private var _binding: FragmentLandingBinding? = null

    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        _binding = FragmentLandingBinding.inflate(inflater, container, false)

        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.landingLoginButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.loginFragment))
        binding.landiingRegisterButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.registerFragment))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}