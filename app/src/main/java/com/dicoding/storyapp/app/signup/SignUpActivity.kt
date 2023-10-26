package com.dicoding.storyapp.app.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.app.ViewModelFactory
import com.dicoding.storyapp.app.login.LoginActivity
import com.dicoding.storyapp.core.data.Result
import com.dicoding.storyapp.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private val registerViewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnRegister.setOnClickListener {
            val nameVal = binding.edRegisterName.text.toString()
            val emailVal = binding.edRegisterEmail.text.toString()
            val passwordVal = binding.edRegisterPassword.text.toString()

            if (binding.edRegisterPassword.error == null && binding.edRegisterEmail.error == null) {
                loadingProcess()
                registerViewModel.register(nameVal, passwordVal, emailVal).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            // Handle loading state
                        }
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val response = result.data
                            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                            // After successful registration, navigate to the login page
                            navigateToLogin()
                        }
                        is Result.Error -> {
                            val errorMessage = result.error
                            wrongDataGiven()
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        playAnimation()
    }

    private fun loadingProcess() {
        binding.progressBar.visibility = View.VISIBLE
        binding.edRegisterName.isCursorVisible = false
        binding.edRegisterEmail.isCursorVisible = false
        binding.edRegisterPassword.isCursorVisible = false
    }

    private fun wrongDataGiven() {
        binding.progressBar.visibility = View.GONE
        binding.edRegisterName.isCursorVisible = true
        binding.edRegisterEmail.isCursorVisible = true
        binding.edRegisterPassword.isCursorVisible = true
    }

    private fun navigateToLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish() // Optional, to close the sign-up activity after moving to the login page
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.edtRegisterName, View.ALPHA, 1f).setDuration(400)
        val registerEditTextLayout = ObjectAnimator.ofFloat(binding.edtRegisterEmail, View.ALPHA, 1f).setDuration(400)
        val passwordTextView = ObjectAnimator.ofFloat(binding.edtRegisterPassword, View.ALPHA, 1f).setDuration(400)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(400)

        AnimatorSet().apply {
            playSequentially(
                nameEditTextLayout,
                registerEditTextLayout,
                passwordTextView,
                register
            )
            startDelay = 100
        }.start()
    }
}