package com.example.freshgrade.ui.authorization.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.cscorner.storyapp.ui.customview.EmailEdit
import com.cscorner.storyapp.ui.customview.NameEdit
import com.cscorner.storyapp.ui.customview.PasswordEdit
import com.cscorner.storyapp.ui.customview.RegisterButton
import com.example.freshgrade.R
import com.example.freshgrade.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var registerButton: RegisterButton
    private lateinit var nameEdit: NameEdit
    private lateinit var emailEdit: EmailEdit
    private lateinit var passwordEdit: PasswordEdit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showLoading(false)
        nameEdit = binding.registerNameEditText
        emailEdit = binding.registerEmailEditText
        passwordEdit = binding.registerPasswordEditText
        registerButton = binding.registerButton
        setRegisterEnable()
        Enabler()

        binding.registerButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.loginFragment))
        binding.LoginLink.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.loginFragment))

        registerButton.setOnClickListener {
            val name = nameEdit.text.toString()
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()


            registerUser(name, email, password)

        }

        playAnimation()

        onBackPress()

    }


    private fun setRegisterEnable() {
        val resultName = nameEdit.text
        val resultEmail = emailEdit.text
        val resultPassword = passwordEdit.text
        registerButton.isEnabled =
            resultName != null && resultEmail != null && resultPassword != null && resultPassword.length >= 6
    }

    private fun registerUser(name: String, email: String, password: String) {

//        registerViewModel.register(name, email, password).observe(this) {
//            when (it) {
//                is Result.Success -> {
//                    showLoading(false)
//                    showDialog(SUCCESS)
//                    startActivity(Intent(this, LoginActivity::class.java))
//                    finishAffinity()
//                }
//                is Result.Loading -> showLoading(true)
//                is Result.Error -> {
//                    showDialog(ERROR)
//                    showLoading(false)
//                }
//            }
//        }

    }

    private fun Enabler() {

        nameEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    binding.registerNameEditTextLayout.error = null
                    setRegisterEnable()
                }
                else {
                    binding.registerNameEditText.error = getString(R.string.error_name)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        emailEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }

            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.registerEmailEditTextLayout.error = null
                    setRegisterEnable()
                } else {
//                        binding.registerEmailEditText.error
                    binding.registerEmailEditText.error =
                        getString(R.string.error_invalid_email)
                }
            }
        })

        passwordEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length < 8) {
                    binding.registerPasswordEditText.error =
                        getString(R.string.error_password)
                } else {
                    binding.registerPasswordEditText.error = null
                    setRegisterEnable()
                }

            }
        })

        registerButton.setOnClickListener {
            showLoading(true)

        }

    }


    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.registerImageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title =
            ObjectAnimator.ofFloat(binding.registerTitleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.registerNameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.registerNameEditTextLayout, View.ALPHA, 1f)
                .setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.registerEmailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.registerEmailEditTextLayout, View.ALPHA, 1f)
                .setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.registerPasswordTextView, View.ALPHA, 1f)
                .setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.registerPasswordEditTextLayout, View.ALPHA, 1f)
                .setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun onBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

    }
}