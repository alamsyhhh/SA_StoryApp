package com.dicoding.storyapp.app.details

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.dicoding.storyapp.app.ViewModelFactory
import com.dicoding.storyapp.core.data.Result
import com.dicoding.storyapp.core.utils.dataconverter
import com.dicoding.storyapp.databinding.ActivityDetailsBinding
import com.google.android.material.snackbar.Snackbar

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    private val detailViewModel by viewModels<DetailsViewModel> {
        ViewModelFactory.getInstance(application)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = detailViewModel.getPreference(this).value

        if (Build.VERSION.SDK_INT >= 33) {
            val id = intent.getStringExtra("id")
            if (id != null) loadDetails(token!!, id)
        } else {
            val id = intent.getStringExtra("id") as String
            loadDetails(token!!, id)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadDetails(token: String, id: String) {
        detailViewModel.storydetail("Bearer $token", id).observe(this) {
            when (it) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.apply {
                        progressBar.visibility = View.GONE
                        val date = it.data.story?.createdAt?.let { it1 -> dataconverter(it1) }
                        dateTv.text = date
                        nameTv.text = it.data.story?.name
                        descTv.text = it.data.story?.description
                        photoIV.load(it.data.story?.photoUrl)
                    }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, it.error, Snackbar.LENGTH_SHORT).show()
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}