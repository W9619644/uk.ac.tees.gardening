package com.example.greenthumb.views.setting

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.Observer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.gardening.R
import com.example.greenthumb.common.isInternetConnected
import com.example.greenthumb.views.setting.ui.theme.GardeningTheme
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SettingActivity2 : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

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

        if(isInternetConnected(this)){
            settingsViewModel.getDetails()
        }else{
            Toast.makeText(this@SettingActivity2,"Please check internet connection", Toast.LENGTH_SHORT).show()
        }

        settingsViewModel.isSuccess.observe(this@SettingActivity2, Observer {
            if(it){
                finish()
            }
        })

        settingsViewModel.isLoading.observe(this@SettingActivity2, Observer {
            if (it) {
                dialog?.setMessage("Please wait")
                dialog?.show()
            } else {
                if (dialog?.isShowing == true) {
                    dialog?.cancel()
                }
            }
        })

        setContent {
            GardeningTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    UploadProfileScreen()
                }
            }
        }
    }


    //Upload Profile
    @Composable
    fun UploadProfileScreen() {

        var username by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Upload Profile",
                fontSize = 40.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            // Big Arrow Icon
            Icon(
                painter = painterResource(id = R.drawable.cloud),
                contentDescription = "Upload Icon",
                modifier = Modifier
                    .size(200.dp)
                    .padding(16.dp)
                    .clickable {
                        if (ContextCompat.checkSelfPermission(
                                this@SettingActivity2,
                                Manifest.permission.CAMERA
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            // Permission not granted, request it
                            ActivityCompat.requestPermissions(
                                this@SettingActivity2,
                                arrayOf(Manifest.permission.CAMERA),
                                SettingActivity2.CAMERA_PERMISSION_REQUEST_CODE
                            )
                        } else {
                            // Permission already granted, proceed with camera operations
                            takePicture()
                        }
                    },
                tint = Color.Black // You can change the color of the arrow icon
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Username TextInputLayout
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "Username Icon",
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(18.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Button
            Button(
                onClick = {
                    settingsViewModel.updateProfile(username, imgPic)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(android.graphics.Color.parseColor("#378805")))
            ) {
                Text(text = "Upload Profile", color = Color.White)
            }
        }
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
                        this@SettingActivity2,
                        "com.example.greenthumb.fileprovider",
                        it
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(intent,CAMERA_REQUEST_CODE)
                }
            }
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


    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        UploadProfileScreen()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == Activity.RESULT_OK) {
            val photoUri = resultData?.data
            if (photoUri != null) {
                if(isInternetConnected(this)){
                    settingsViewModel.uploadImage(photoUri)
                }else{
                    Toast.makeText(this@SettingActivity2,"Please check internet connection", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this@SettingActivity2,"Please check internet connection", Toast.LENGTH_SHORT).show()
                }


            }catch (e:java.lang.Exception){
                e.printStackTrace()
            }

        }
    }
}


