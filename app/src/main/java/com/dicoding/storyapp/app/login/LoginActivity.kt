package com.dicoding.storyapp.app.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.app.ViewModelFactory
import com.dicoding.storyapp.app.home.MainActivity
import com.dicoding.storyapp.app.signup.SignUpActivity
import com.dicoding.storyapp.core.data.Result
import com.dicoding.storyapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        isLoginBefore(this)

        val edEmail = binding.edLoginEmail
        val edPassword = binding.edLoginPassword

        binding.btnLogin.setOnClickListener {
            //Check If password kosong
            if (edPassword.text?.isEmpty() == true) {
                edPassword.error = "Password cannot be empty"
            }

            if (edEmail.text?.isEmpty() == true) {
                edEmail.error = "Email cannot be empty"
            }

            if (edPassword.error == null && edEmail.error == null) {
                userLogin(edEmail.text.toString(), edPassword.text.toString())
            }

        }
        binding.btnRegister.setOnClickListener {
            val register = Intent(this, SignUpActivity::class.java)
            startActivity(register)
        }
        playAnimation()
    }

    private fun userLogin(email: String, password: String) {
        loginViewModel.login(email, password).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        loadingProcess()
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val data = result.data
                        Toast.makeText(this@LoginActivity, data.message, Toast.LENGTH_SHORT).show()
                        if (data.loginResult?.token != null) {
                            loginViewModel.setPreference(data.loginResult.token, this)
                        }
                        val mainActivity = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(mainActivity)
                        finish()
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Email and Password you entered are incorrect", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    private fun loadingProcess() {
        binding.progressBar.visibility = View.VISIBLE
        binding.edLoginEmail.isCursorVisible = false
        binding.edLoginPassword.isCursorVisible = false
    }

    private fun isLoginBefore(context: Context) {
        loginViewModel.getPreference(context).observe(this) { token ->
            if (token?.isEmpty() == false) {
                val mainActivity = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(mainActivity)
                finish()
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -40f, 40f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.edtEmailLayout, View.ALPHA, 1f).setDuration(400)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.edtPasswordLayout, View.ALPHA, 1f).setDuration(400)
        val haveAccount = ObjectAnimator.ofFloat(binding.HaveAccount, View.ALPHA, 1f).setDuration(400)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(400)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(400)

        AnimatorSet().apply {
            playSequentially(
                emailEditTextLayout,
                passwordTextView,
                haveAccount,
                register,
                login
            )
            startDelay = 500
        }.start()
    }

}