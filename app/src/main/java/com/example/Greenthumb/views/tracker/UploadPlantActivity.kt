package com.example.greenthumb.views.tracker

import android.Manifest
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.gardening.databinding.ActivityUploadPlantBinding
import com.example.greenthumb.common.isInternetConnected

import com.example.greenthumb.views.setting.SettingsActivity
import com.example.greenthumb.workmanager.CustomBroadcast
import com.example.greenthumb.workmanager.NotificationWorker
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class UploadPlantActivity : AppCompatActivity() {

    lateinit var binding : ActivityUploadPlantBinding
    private val uploadPlantViewModel:UploadPlantViewModel by viewModels()

    private val PERMISSION_GALLERY_CODE = 2222
    private val LOAD_IMAGE_RESULTS = 3333
    private var imgPic :String =""
    private var dialog: ProgressDialog? = null

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
        private const val CAMERA_REQUEST_CODE = 101
    }
    private var currentPhotoPath: String? = null


    private lateinit var alarmManager: AlarmManager
    private lateinit var notificationIntent: Intent
    private lateinit var pendingIntent: PendingIntent


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialog = ProgressDialog(this@UploadPlantActivity)

        binding.idImagebtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission not granted, request it
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    SettingsActivity.CAMERA_PERMISSION_REQUEST_CODE
                )
            } else {
                // Permission already granted, proceed with camera operations
                takePicture()
            }
        }


        binding.idAlarm.setOnClickListener {
       //     getNotification("8")
        }

        showConfirmationDialog()
        binding.idUpload.setOnClickListener {


          //  setworkManager(getTime)


            val desc = binding.idDescription.text.toString() ?: ""

            if(isInternetConnected(this)){
                uploadPlantViewModel.uploadPlant(desc,imgPic)
            }else{
                Toast.makeText(this@UploadPlantActivity,"Please check internet connection", Toast.LENGTH_SHORT).show()
            }



        }

        uploadPlantViewModel.firebaseImage.observe(this , Observer {
            imgPic = it
        })

        uploadPlantViewModel.success.observe(this@UploadPlantActivity, Observer {
            if(it){
                finish()
            }
        })
        uploadPlantViewModel.isLoading.observe(this@UploadPlantActivity, Observer {
            if(it){
                dialog?.setMessage("Please wait....")
                dialog?.show()
            }else{
                if(dialog?.isShowing == true){
                    dialog?.cancel()
                }
            }
        }
        )
    }

    private fun setworkManager( ) {

        val timeInput = "16:50" // Example time input, format: "HH:mm"
        val calendar = Calendar.getInstance()
        val currentTime = calendar.timeInMillis

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val selectedTime = timeFormat.parse(timeInput)

        if (selectedTime != null) {
            calendar.time = selectedTime

            if (calendar.timeInMillis < currentTime) {
                calendar.add(Calendar.DAY_OF_YEAR, 1) // Schedule for the next day
            }

            val timeDiffInMillis = calendar.timeInMillis - currentTime

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val dailyWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(timeDiffInMillis, TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(this).enqueue(dailyWorkRequest)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getNotification() {

        val timeInput =  "08:00"// Example time input, format: "HH:mm"
        val calendar = Calendar.getInstance()
        val currentTime = calendar.timeInMillis

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val selectedTime = timeFormat.parse(timeInput)

        if (selectedTime != null) {
            calendar.time = selectedTime

            alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            notificationIntent = Intent(this, CustomBroadcast::class.java)

            val flags = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                else -> PendingIntent.FLAG_UPDATE_CURRENT
            }

            pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                notificationIntent,
                flags
            )



            val intervalMillis = 30_000 // 1 minute

            val triggerTimeMillis = System.currentTimeMillis() + intervalMillis

/*
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
*/

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent); // +100 is to set nearly to the current time

            /*
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMillis,
                intervalMillis.toLong(),
                pendingIntent
            )*/
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
                        "com.example.gardening.fileprovider",
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

            SettingsActivity.CAMERA_PERMISSION_REQUEST_CODE -> {
                if (requestCode == SettingsActivity.CAMERA_PERMISSION_REQUEST_CODE) {
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Confirmation")
            .setMessage("Do you want to Notificaion for watering?")
            .setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
               callLocationNotification()
             //   setworkManager()
                dialog?.cancel()
            }
            .setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
                // Handle "No" button click
                // P erform the desired action or dismiss the dialog
                if(dialog?.isShowing == true){
                    dialog?.cancel()
                }
            }
            .setCancelable(false) // Set this to true if you want to enable canceling the dialog by tapping outside

        val dialog = builder.create()
        dialog.show()
    }

    private fun callLocationNotification() {
        // Get the current time and set it to 5 pm
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 7)

        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        // Check if the current time is already past 5 pm
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            // Increment the day by 1 to schedule the work for the following day
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // Set the initial delay for the work request
        val initialDelay = calendar.timeInMillis - System.currentTimeMillis()

        // Create the periodic work request
        val locationWorkRequest = PeriodicWorkRequest.Builder(
            NotificationWorker::class.java,
            1, TimeUnit.DAYS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        // Enqueue the work request
        WorkManager.getInstance(this).enqueue(locationWorkRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK) {
            val photoUri = resultData?.data



            if(photoUri!=null){
                if(isInternetConnected(this)){
                    uploadPlantViewModel.uploadImage(photoUri)
                }else{
                    Toast.makeText(this@UploadPlantActivity,"Please check internet connection", Toast.LENGTH_SHORT).show()
                }




            }

        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            try {



                val datafile = File(currentPhotoPath)
                val outputFileUri = Uri.fromFile(datafile)
                if(isInternetConnected(this)){
                    uploadPlantViewModel.uploadImage(outputFileUri)
                }else{
                    Toast.makeText(this@UploadPlantActivity,"Please check internet connection", Toast.LENGTH_SHORT).show()
                }

                try {
                    if (datafile.exists()) {
                        val bitmap = BitmapFactory.decodeFile(datafile.absolutePath)
                        binding.idImagebtn.setImageBitmap(bitmap)
                    }
                }catch (e:Exception){

                }

            }catch (e: Exception){
                e.printStackTrace()
            }

        }
    }

}