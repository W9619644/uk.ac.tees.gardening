package com.example.greenthumb.views.setting

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.example.gardening.databinding.ActivitySettingsBinding
import com.example.greenthumb.common.isInternetConnected
import com.squareup.picasso.Picasso
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class SettingsActivity : AppCompatActivity() {


    private val settingsViewModel: SettingsViewModel by viewModels()

    lateinit var binding: ActivitySettingsBinding
    private val PERMISSION_GALLERY_CODE = 2222
    private val LOAD_IMAGE_RESULTS = 3333
    private var imgPic: String = ""
    private var dialog: ProgressDialog? = null

    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 100
        private const val CAMERA_REQUEST_CODE = 101
    }
    private var currentPhotoPath: String? = null


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(isInternetConnected(this)){
            settingsViewModel.getDetails()
        }else{
            Toast.makeText(this@SettingsActivity,"Please check internet connection", Toast.LENGTH_SHORT).show()
        }


        dialog = ProgressDialog(this@SettingsActivity)
        binding.profileImage.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission not granted, request it
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            } else {
                // Permission already granted, proceed with camera operations
                takePicture()
            }
            //openGallery()
        }
        binding.idUpdate.setOnClickListener {
            val username = binding.idUsername.text.toString() ?: ""
            settingsViewModel.updateProfile(username, imgPic)
        }

        settingsViewModel.firebaseImage.observe(this@SettingsActivity, Observer {
            if(!it.isNullOrEmpty()){
                Picasso.get().load(it).into(binding.profileImage)

                imgPic = it
            }

        })

        settingsViewModel.name.observe(this@SettingsActivity, Observer {
            if(!it.isNullOrEmpty()){
                binding.idUsername.setText(it)
            }

        })

        settingsViewModel.isSuccess.observe(this@SettingsActivity, Observer {
            if(it){
                finish()
            }
        })

        settingsViewModel.isLoading.observe(this@SettingsActivity, Observer {
            if (it) {
                dialog?.setMessage("Please wait")
                dialog?.show()
            } else {
                if (dialog?.isShowing == true) {
                    dialog?.cancel()
                }
            }
        })
    }

    private fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Handle the exception
                    null
                }

                photoFile?.also {
                    val photoURI = FileProvider.getUriForFile(
                        this,
                        "com.example.greenthumb.fileprovider",
                        it
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(intent, CAMERA_REQUEST_CODE)
                }
            }
        }
    }


        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private fun openGallery() {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                requestPermissions(permissions, PERMISSION_GALLERY_CODE)
            } else {
                openImage();
            }
        }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(null)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }
        private fun openImage() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, LOAD_IMAGE_RESULTS)
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            when (requestCode) {
                PERMISSION_GALLERY_CODE -> {
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        openImage()
                    } else {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }

                CAMERA_PERMISSION_REQUEST_CODE -> {
                    if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
                        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            // Camera permission granted, proceed with camera operations
                            takePicture()
                        } else {
                            // Camera permission denied, handle the situation gracefully (e.g., show a message)
                            // or disable camera-related functionality
                        }
                    }
                }
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
            super.onActivityResult(requestCode, resultCode, resultData)
            if (requestCode == LOAD_IMAGE_RESULTS && resultCode == Activity.RESULT_OK) {
                val photoUri = resultData?.data
                if (photoUri != null) {
                    if(isInternetConnected(this)){
                        settingsViewModel.uploadImage(photoUri)
                    }else{
                        Toast.makeText(this@SettingsActivity,"Please check internet connection", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
                try {
                    val datafile = File(currentPhotoPath)
                    val outputFileUri = Uri.fromFile(datafile)
                    if(isInternetConnected(this)){
                        settingsViewModel.uploadImage(outputFileUri)
                    }else{
                        Toast.makeText(this@SettingsActivity,"Please check internet connection", Toast.LENGTH_SHORT).show()
                    }


                }catch (e:java.lang.Exception){
                    e.printStackTrace()
                }

            }
        }


}

