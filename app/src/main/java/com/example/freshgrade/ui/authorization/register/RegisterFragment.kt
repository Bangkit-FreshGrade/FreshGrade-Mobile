package com.example.freshgrade.ui.authorization.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cscorner.storyapp.ui.customview.EmailEdit
import com.cscorner.storyapp.ui.customview.FirstNameEdit
import com.cscorner.storyapp.ui.customview.LastNameEdit
import com.cscorner.storyapp.ui.customview.NameEdit
import com.cscorner.storyapp.ui.customview.PasswordEdit
import com.cscorner.storyapp.ui.customview.RegisterButton
import com.example.freshgrade.R
import com.example.freshgrade.data.api.Result
import com.example.freshgrade.databinding.FragmentRegisterBinding
import com.example.freshgrade.ui.util.ViewModelFactory


class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var registerButton: RegisterButton
    private lateinit var firstNameEdit: FirstNameEdit
    private lateinit var lastNameEdit: LastNameEdit
    private lateinit var nameEdit: NameEdit
    private lateinit var emailEdit: EmailEdit
    private lateinit var passwordEdit: PasswordEdit

    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(false)
        initializeViews()
        setRegisterEnable()
        enableTextWatchers()
        setOnClickListeners()
        playAnimation()
        handleBackPress()
    }

    private fun initializeViews() {
        nameEdit = binding.registerUserNameEditText
        firstNameEdit = binding.registerFirstNameEditText
        lastNameEdit = binding.registerLastNameEditText
        emailEdit = binding.registerEmailEditText
        passwordEdit = binding.registerPasswordEditText
        registerButton = binding.registerButton
    }

    private fun setRegisterEnable() {
        val isNameNotEmpty = nameEdit.text?.isNotEmpty() == true
        val isFirstNameNotEmpty = firstNameEdit.text?.isNotEmpty() == true
        val isLastNameNotEmpty = lastNameEdit.text?.isNotEmpty() == true
        val isEmailValid = emailEdit.text?.let { android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() } == true
        val isPasswordValid = (passwordEdit.text?.length ?: 0) >= 8

        registerButton.isEnabled = isNameNotEmpty && isFirstNameNotEmpty && isLastNameNotEmpty && isEmailValid && isPasswordValid
    }

    private fun registerUser(email: String, password: String, userName: String, firstName: String, lastName: String) {
        registerViewModel.register(email, password, userName, firstName, lastName).observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {
                    showLoading(false)
                    showDialog(ERROR)
                    Toast.makeText(context, it.error, Toast.LENGTH_LONG).show()
                }
                Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    showDialog(SUCCESS)
                }
            }
        }
    }

    private fun showDialog(mode: String) {
        val builder = AlertDialog.Builder(requireContext())
        if (mode == ERROR) {
            val title = ERROR
            val message = R.string.register_failed
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton(android.R.string.ok) { _, _ ->
            }
            builder.show()
        } else if (mode == SUCCESS) {
            Toast.makeText( requireContext(), getString(R.string.register_success), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun enableTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setRegisterEnable()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        nameEdit.addTextChangedListener(textWatcher)
        firstNameEdit.addTextChangedListener(textWatcher)
        lastNameEdit.addTextChangedListener(textWatcher)
        emailEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    binding.registerEmailEditTextLayout.error = null
                } else {
                    binding.registerEmailEditText.error = getString(R.string.error_invalid_email)
                }
                setRegisterEnable()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        passwordEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length < 8) {
                    binding.registerPasswordEditText.error = getString(R.string.error_password)
                } else {
                    binding.registerPasswordEditTextLayout.error = null
                }
                setRegisterEnable()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setOnClickListeners() {
        binding.registerButton.setOnClickListener {
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()
            val userName = nameEdit.text.toString()
            val firstName = firstNameEdit.text.toString()
            val lastName = lastNameEdit.text.toString()
            registerUser(email, password, userName, firstName, lastName)
        }
        binding.LoginLink.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.registerImageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val animatorSet = AnimatorSet().apply {
            playSequentially(
                ObjectAnimator.ofFloat(binding.registerImageView, View.ALPHA, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.sloganTextView, View.ALPHA, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.registerTitleTextView, View.ALPHA, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.container, View.ALPHA, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.registerFirstNameTextView, View.ALPHA, 1f).setDuration(100),
                ObjectAnimator.ofFloat(binding.registerFirstNameEditTextLayout, View.ALPHA, 1f).setDuration(100),
                ObjectAnimator.ofFloat(binding.registerLastNameTextView, View.ALPHA, 1f).setDuration(100),
                ObjectAnimator.ofFloat(binding.registerLastNameEditTextLayout, View.ALPHA, 1f).setDuration(100),
                ObjectAnimator.ofFloat(binding.registerUserNameTextView, View.ALPHA, 1f).setDuration(100),
                ObjectAnimator.ofFloat(binding.registerUserNameEditTextLayout, View.ALPHA, 1f).setDuration(100),
                ObjectAnimator.ofFloat(binding.registerEmailTextView, View.ALPHA, 1f).setDuration(100),
                ObjectAnimator.ofFloat(binding.registerEmailEditTextLayout, View.ALPHA, 1f).setDuration(100),
                ObjectAnimator.ofFloat(binding.registerPasswordTextView, View.ALPHA, 1f).setDuration(100),
                ObjectAnimator.ofFloat(binding.registerPasswordEditTextLayout, View.ALPHA, 1f).setDuration(100),
                ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(100)
            )
            startDelay = 100
        }
        animatorSet.start()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ERROR = "error"
        const val SUCCESS = "success"
    }
}