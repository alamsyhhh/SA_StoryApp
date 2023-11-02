package com.dicoding.storyapp.app.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.app.ViewModelFactory
import com.dicoding.storyapp.app.addstory.AddStoryActivity
import com.dicoding.storyapp.app.home.paging.StoryPagingAdapter
import com.dicoding.storyapp.app.login.LoginActivity
import com.dicoding.storyapp.app.maps.MapsActivity
import com.dicoding.storyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MenuItem.OnMenuItemClickListener {

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        val token  = mainViewModel.getPreference(this).value

        loadStory(token!!)

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val change = menu.findItem(R.id.change)
        change.setOnMenuItemClickListener(this)

        val logout = menu.findItem(R.id.logout)
        logout.setOnMenuItemClickListener(this)

        val maps = menu.findItem(R.id.maps)
        maps.setOnMenuItemClickListener (this)

        return true
    }

    override fun onMenuItemClick(p0: MenuItem): Boolean {
        return when (p0.itemId) {

            R.id.change -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }

            R.id.logout -> {
                showLogoutConfirmationDialog()
                true
            }

            R.id.maps -> {
                startActivity(Intent(this,MapsActivity::class.java))
                true
            }

            else -> true
        }
    }

    private fun showLogoutConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Are you sure you want to logout?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                logout()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Logout Confirmation")
        alert.show()
    }

    private fun logout() {
        mainViewModel.setPreference("", this)
        val loginActivity = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(loginActivity)
        finish()
    }

    private fun loadStory(token: String){
        val adapter = StoryPagingAdapter()
        binding.rvStory.adapter = adapter

        mainViewModel.getStories(token).observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }
}
