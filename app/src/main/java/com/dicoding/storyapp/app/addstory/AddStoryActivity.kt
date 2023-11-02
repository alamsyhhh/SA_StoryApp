package com.dicoding.storyapp.app.addstory

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dicoding.storyapp.R
import com.dicoding.storyapp.app.ViewModelFactory
import com.dicoding.storyapp.app.home.MainActivity
import com.dicoding.storyapp.app.login.LoginViewModel
import com.dicoding.storyapp.core.data.Result
import com.dicoding.storyapp.core.utils.getImageUri
import com.dicoding.storyapp.core.utils.reduceFileImage
import com.dicoding.storyapp.core.utils.uriToFile
import com.dicoding.storyapp.databinding.ActivityAddStoryBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var progressBar: ProgressBar

    private var currentImageUri: Uri? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var getFile: File? = null

    //LOCATION
    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val addStoryViewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = loginViewModel.getPreference(this)
        progressBar = binding.progressBar

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.cameraBtn.setOnClickListener {
            startCamera()
        }
        binding.galeryBtn.setOnClickListener {
            startGallery()
        }

        binding.btnPostStory.setOnClickListener {
            uploadImage(binding.edDeskripsi, token.value.toString())
        }

        //Location
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // initialize fused location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //checkbox
        val checkBox = binding.checkbox
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getCurrentLocation()

            } else {
                longitude = null
                latitude = null
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onBackPressed() {
        val confirmDialog = AlertDialog.Builder(this)
        confirmDialog.setTitle(getString(R.string.confirmation))
        confirmDialog.setMessage(getString(R.string.confirm_cancel_upload))

        confirmDialog.setPositiveButton(getString(R.string.yes)) { _, _ ->
            super.onBackPressed()
        }

        confirmDialog.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }

        confirmDialog.show()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri)
        launcherIntentCamera.launch(intent)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        currentImageUri?.let {
            val uriToFile = uriToFile(it, this)
            getFile = uriToFile
        }
        showImage()
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.imageStoryUpload.setImageURI(it)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddStoryActivity)
                getFile = myFile
                binding.imageStoryUpload.setImageURI(uri)
            }
        }
    }

    private fun uploadImage(getdesc: EditText, token: String) {
        if (getFile != null) {
            progressBar.visibility = View.VISIBLE

            val file = reduceFileImage(getFile as File)
            val desc = getdesc.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            addStoryViewModel.addNewStory("Bearer $token", imageMultipart, desc, latitude?.toFloat(), longitude?.toFloat())
                .observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            // Sudah menampilkan ProgressBar, tidak perlu mengubahnya di sini
                        }
                        is Result.Success -> {
                            progressBar.visibility = View.GONE
                            val response = result.data
                            val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        is Result.Error -> {
                            progressBar.visibility = View.GONE
                            val errorMessage = result.error
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        } else {
            Toast.makeText(
                this@AddStoryActivity,
                getString(R.string.select_image_file),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    //LOCATION
    private fun getCurrentLocation() {
        // checking location permission
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // request permission
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_PERMISSIONS)

            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // getting the last known or current location
                latitude = location.latitude
                longitude = location.longitude
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed on getting current location",
                    Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}