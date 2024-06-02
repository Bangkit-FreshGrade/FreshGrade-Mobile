package com.example.freshgrade.ui.authorization.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.cscorner.storyapp.ui.customview.EmailEdit
import com.cscorner.storyapp.ui.customview.LoginButton
import com.cscorner.storyapp.ui.customview.PasswordEdit
import com.example.freshgrade.R
import com.example.freshgrade.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginButton: LoginButton
    private lateinit var emailEdit: EmailEdit
    private lateinit var passwordEdit: PasswordEdit


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
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

        Enabler()


        binding.loginButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.mainActivity))

        binding.RegisterLink.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.registerFragment))

        loginButton.setOnClickListener {
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()

            authenticate(email, password)
        }

        playAnimation()

        onBackPress()
    }


    private fun setLoginButtonEnable() {
        val email = emailEdit.text
        val password = passwordEdit.text
        loginButton.isEnabled = email != null && email.toString()
            .isNotEmpty() && password != null && password.toString().isNotEmpty()
    }

    private fun Enabler(){
        emailEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.loginEmailEditTextLayout.error = null
                    setLoginButtonEnable()
                } else {
                    binding.loginEmailEditText.error = getString(R.string.error_invalid_email)
//                    binding.loginEmailEditText.error = ""
//                    Toast.makeText(context, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        passwordEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length < 8) {
                    binding.loginPasswordEditText.error = getString(R.string.error_password)
                } else {
                    binding.loginPasswordEditText.error = null
                    setLoginButtonEnable()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })



    }

    private fun authenticate(email: String, password: String) {

//        loginViewModel.userLogin(email, password).observe(this) {
//            when (it) {
//                is Result.Success -> {
//                    showLoading(false)
//                    dataStore
//                    val response = it.data
//                    loginViewModel.saveUserData(
//                        UserModel(
//                            response.loginResult?.name.toString(),
//                            response.loginResult?.token.toString(),
//                            true
//                        )
//                    )
//
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finishAffinity()
//                }
//                is Result.Loading -> showLoading(true)
//                is Result.Error -> {
//                    showDialog(ERROR)
//                    showLoading(false)
//                }
//            }
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

//    private fun setupView() {
//        @Suppress("DEPRECATION")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.hide(WindowInsets.Type.statusBars())
//        } else {
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            )
//        }
//        supportActionBar?.hide()
//    }

    private fun onBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {


    }
}