package com.dicoding.storyapp.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.app.login.LoginActivity
import com.dicoding.storyapp.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var bindingSplash: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSplash = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(bindingSplash.root)

        supportActionBar?.hide()

        val handler = Handler(mainLooper)
        handler.postDelayed({
            val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, DELAY_TIME)
    }

    companion object {
        private const val DELAY_TIME = 1500L
    }
}