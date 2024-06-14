package com.example.freshgrade.ui.authorization.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.cscorner.storyapp.ui.customview.EmailEdit
import com.cscorner.storyapp.ui.customview.LoginButton
import com.cscorner.storyapp.ui.customview.PasswordEdit
import com.example.freshgrade.R
import com.example.freshgrade.data.api.Result
import com.example.freshgrade.data.pref.UserModel
import com.example.freshgrade.databinding.FragmentLoginBinding
import com.example.freshgrade.ui.util.ViewModelFactory


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginButton: LoginButton
    private lateinit var emailEdit: EmailEdit
    private lateinit var passwordEdit: PasswordEdit

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showLoading(false)

        loginButton = binding.loginButton
        emailEdit = binding.loginEmailEditText
        passwordEdit = binding.loginPasswordEditText
        setLoginButtonEnable()

        enabler()

        binding.RegisterLink.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.registerFragment))

        loginButton.setOnClickListener {
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()
            authenticate(email, password)
        }

//        binding.ChangePasswordLink.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.changePasswordFragment))

        playAnimation()
        onBackPress()
    }

    private fun setLoginButtonEnable() {
        val email = emailEdit.text
        val password = passwordEdit.text
        loginButton.isEnabled = email != null && email.toString()
            .isNotEmpty() && password != null && password.toString().isNotEmpty()
    }

    private fun enabler(){
        emailEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.loginEmailEditText.error = null
                    setLoginButtonEnable()
                } else {
                    binding.loginEmailEditText.error
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        passwordEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length < 8) {
                    binding.loginPasswordEditText.error
                } else {
                    binding.loginPasswordEditText.error = null
                    setLoginButtonEnable()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun authenticate(email: String, password: String) {
        loginViewModel.userLogin(email, password).observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {
                    showLoading(false)
                    showDialog(ERROR)
                    Toast.makeText(context, it.error, Toast.LENGTH_LONG).show()

                }

                Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    val response = it.data
                    loginViewModel.saveUserData(
                        UserModel(
                            response.accessToken,
                            true
                        )
                    )
                    showDialog(SUCCESS)
                    findNavController().navigate(R.id.mainActivity)
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.loginImageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.loginTitleTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.loginEmailText, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.loginEmailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.LoginPasswordText, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.loginPasswordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        val registerText =
            ObjectAnimator.ofFloat(binding.RegisterText, View.ALPHA, 1f).setDuration(100)
        val registerLink =
            ObjectAnimator.ofFloat(binding.RegisterLink, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login,
                registerText,
                registerLink
            )
            startDelay = 100
        }.start()
    }

    private fun onBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialog(mode: String, message: String? = null) {
        val builder = AlertDialog.Builder(requireContext())
        if (mode == ERROR) {
            val title = getString(R.string.login_failed)
            val msg = message ?: getString(R.string.login_failed)
            builder.setTitle(title)
            builder.setMessage(msg)
            message?.let { builder.setNeutralButton(android.R.string.ok) { _, _ -> } }
            builder.setPositiveButton(android.R.string.ok) { _, _ -> }
            builder.show()
        } else if (mode == SUCCESS) {
            Toast.makeText(requireContext(), getString(R.string.login_success), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ERROR = "error"
        const val SUCCESS = "success"
    }

}