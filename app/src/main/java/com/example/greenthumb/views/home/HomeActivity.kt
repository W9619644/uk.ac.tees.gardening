package com.example.greenthumb.views.home

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.gardening.R
import com.example.gardening.databinding.ActivityHomeBinding

import com.example.greenthumb.common.ShakeDetector
import com.example.greenthumb.common.isInternetConnected
import com.example.greenthumb.common.navigateActivity

import com.example.greenthumb.views.library.PlantsLibraryActivity
import com.example.greenthumb.views.login.LoginActivity
import com.example.greenthumb.views.login.LoginActivity2
import com.example.greenthumb.views.setting.SettingsActivity
import com.example.greenthumb.views.tips.TipsArticleActivity
import com.example.greenthumb.views.tracker.PlantTrackerActivity
import com.example.greenthumb.workmanager.CustomBroadcast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.util.*


class HomeActivity : AppCompatActivity() {

    lateinit var binding : ActivityHomeBinding

    private lateinit var alarmManager: AlarmManager
    private lateinit var notificationIntent: Intent
    private lateinit var pendingIntent: PendingIntent

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showAppClosingDialog()
        }
    }


    private val homeActivityViewModel:HomeActivityViewModel by viewModels()
    private val imageUrls = listOf(
        "https://d2seqvvyy3b8p2.cloudfront.net/40ab8e7cdddbe3e78a581b84efa4e893.jpg",
        "https://d2seqvvyy3b8p2.cloudfront.net/40ab8e7cdddbe3e78a581b84efa4e893.jpg",
        "https://d2seqvvyy3b8p2.cloudfront.net/40ab8e7cdddbe3e78a581b84efa4e893.jpg",
        "https://d2seqvvyy3b8p2.cloudfront.net/40ab8e7cdddbe3e78a581b84efa4e893.jpg",

        // Add more image URLs as needed
    )

    private lateinit var shakeDetector: ShakeDetector
    private fun showAppClosingDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Warning")
            .setMessage("Do you really want to tclose the app?")
            .setPositiveButton("Yes") { _, _ ->     finishAffinity();
            }
            .setNegativeButton("No", null)
            .show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)


        setContentView(binding.root)

        if(isInternetConnected(this)){
            homeActivityViewModel.getDetails()
        }else{
            Toast.makeText(this@HomeActivity,"Please check internet connection", Toast.LENGTH_SHORT).show()
        }


        shakeDetector = ShakeDetector(this) {
            showRandomImage()
        }
/*


        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        notificationIntent = Intent(this, CustomBroadcast::class.java)

        val flags = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
            else -> FLAG_UPDATE_CURRENT
        }

        pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            notificationIntent,
            flags
        )

        val intervalMillis = 30_000 // 1 minute

        val triggerTimeMillis = System.currentTimeMillis() + intervalMillis

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTimeMillis,
            intervalMillis.toLong(),
            pendingIntent
        )
*/



        //  broadcastrecievercustmom()


/*
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val repeatInterval = 2L // 10 minutes

        val periodicWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval,
            TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(periodicWorkRequest)
*/


        homeActivityViewModel.firebaseImage.observe(this@HomeActivity , Observer {
            if(it.isNotEmpty()){
                Picasso.get().load(it).into(binding.profileImage)
            }
        })

        homeActivityViewModel.name.observe(this@HomeActivity , Observer {
            if(it.isNotEmpty()){
               binding.idUsername.setText(""+it)
            }
        })

        binding.idPlantLibrary.setOnClickListener {
            navigateActivity(this@HomeActivity,PlantsLibraryActivity::class.java)
        }


        binding.idPlantTracker.setOnClickListener {
            navigateActivity(this@HomeActivity,PlantTrackerActivity::class.java)
        }



        binding.idTips.setOnClickListener {
            navigateActivity(this@HomeActivity,TipsArticleActivity::class.java)
        }

        binding.idSettings.setOnClickListener {
            navigateActivity(this@HomeActivity,SettingsActivity::class.java)
        }

        binding.idLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            navigateActivity(this@HomeActivity,LoginActivity2::class.java)
            finish()
        }


    }

    private fun broadcastrecievercustmom() {
        notification_channel();


        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        notificationIntent = Intent(this, CustomBroadcast::class.java)
        pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


    }

    private fun notification_channel() {

        val intervalMillis = 30_000 // 1 minute

        val triggerTimeMillis = System.currentTimeMillis() + intervalMillis

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTimeMillis,
            intervalMillis.toLong(),
            pendingIntent
        )



        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTimeMillis,
            intervalMillis.toLong(),
            pendingIntent
        )
     /*   val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val selectedTime = timeFormat.parse("timeInput")

        if (selectedTime != null) {
            val calendar = Calendar.getInstance()
            calendar.time = selectedTime

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

            Toast.makeText(this, "Notification scheduled at $timeInput", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Invalid time format", Toast.LENGTH_SHORT).show()
        }*/
    }

    override fun onResume() {
        super.onResume()
        shakeDetector.start()
    }

    override fun onPause() {
        super.onPause()
        shakeDetector.stop()
    }
    private fun showRandomImage() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_image)
       /* val image= dialog.findViewById<ImageView>(R.id.imagevv)
        Picasso.get().load("https://d2seqvvyy3b8p2.cloudfront.net/40ab8e7cdddbe3e78a581b84efa4e893.jpg").into(image)
     */   // Customize the dialog layout and content as needed
        dialog.show()


    }
}