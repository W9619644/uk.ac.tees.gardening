package com.example.greenthumb.views.home

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.greenthumb.views.home.ui.theme.GardeningTheme
import androidx.lifecycle.Observer
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.example.gardening.R
import com.example.gardening.databinding.ActivityHomeBinding

import com.example.greenthumb.common.ShakeDetector
import com.example.greenthumb.common.isInternetConnected
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.example.greenthumb.common.navigateActivity
import com.example.greenthumb.views.library.PlantsLibraryActivity
import com.example.greenthumb.views.login.LoginActivity2
import com.example.greenthumb.views.setting.SettingActivity2
import com.example.greenthumb.views.tips.TipsArticleActivity
import com.example.greenthumb.views.tracker.PlantTrackerActivity
import com.google.firebase.auth.FirebaseAuth


class HomeActivity2 : ComponentActivity() {

    lateinit var binding : ActivityHomeBinding

    private lateinit var alarmManager: AlarmManager
    private lateinit var notificationIntent: Intent
    private lateinit var pendingIntent: PendingIntent

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showAppClosingDialog()
        }
    }

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
    private val homeActivityViewModel:HomeActivityViewModel by viewModels()
    private fun showRandomImage() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_image)
        /* val image= dialog.findViewById<ImageView>(R.id.imagevv)
         Picasso.get().load("https://d2seqvvyy3b8p2.cloudfront.net/40ab8e7cdddbe3e78a581b84efa4e893.jpg").into(image)
      */   // Customize the dialog layout and content as needed
        dialog.show()


    }
    var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(isInternetConnected(this)){
            homeActivityViewModel.getDetails()
        }else{
            Toast.makeText(this@HomeActivity2,"Please check internet connection", Toast.LENGTH_SHORT).show()
        }
        shakeDetector = ShakeDetector(this) {
            showRandomImage()
        }

        homeActivityViewModel.name.observe(this@HomeActivity2 , Observer {
            if(it.isNotEmpty()){
                name = it
            }
        })

        setContent {
            GardeningTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Home()

                }
            }
        }
    }

    @Composable
    fun Home() {
        var username by remember { mutableStateOf("") }
        homeActivityViewModel.name.observe(this@HomeActivity2 , Observer {
            if(it.isNotEmpty()){
                username = it
            }
        })
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                elevation = 0.dp,
                backgroundColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()), // Adjust the height as needed
                    verticalArrangement = Arrangement.Top
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Profile Image
                        Image(
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = null,
                            modifier = Modifier
                                .height(30.dp)
                        )

                        // Username
                        Text(
                            text = username,
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(start = 16.dp)
                        )

                        // Fast Forward Icon
                        Image(
                            painter = painterResource(id = R.drawable.logout),
                            contentDescription = null,
                            modifier = Modifier
                                .height(20.dp)
                                .padding(start = 16.dp).clickable {
                                    FirebaseAuth.getInstance().signOut()
                                    navigateActivity(this@HomeActivity2, LoginActivity2::class.java)
                                    finish()
                                }
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // The garden suggests text
                    Text(
                        text = "The garden suggests there might be a place where we can meet nature halfway",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        letterSpacing = 0.1.sp,
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )

                    // Love nature text
                    Text(
                        text = "Love nature",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        letterSpacing = 0.1.sp,
                        modifier = Modifier
                            .padding(top = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(35.dp))

                    // Cards below "Love nature" text in a column
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        // Card 1
                        ImageWithText(
                            imageId = R.drawable.bookshelf,
                            name = "Plant Library"
                        ){
                            navigateActivity(this@HomeActivity2, PlantsLibraryActivity::class.java)
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        // Card 2
                        ImageWithText(
                            imageId = R.drawable.plant_tracker1,
                            name = "Plant Tracker"
                        ){
                            navigateActivity(this@HomeActivity2, PlantTrackerActivity::class.java)
                        }
                        Spacer(modifier = Modifier.height(15.dp))

                        ImageWithText(
                            imageId = R.drawable.article,
                            name = "Articles"
                        ){
                            navigateActivity(this@HomeActivity2, TipsArticleActivity::class.java)
                        }
                        Spacer(modifier = Modifier.height(15.dp))

                        // Card 4
                        ImageWithText(
                            imageId = R.drawable.setting,
                            name = "Settings"
                        ){
                            navigateActivity(this@HomeActivity2, SettingActivity2::class.java)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ImageWithText(imageId: Int, name: String, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth().height(130.dp)
                .padding(4.dp).clickable { onClick() },
            elevation = 4.dp,
            backgroundColor = Color(android.graphics.Color.parseColor("#e9fedd"))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Image
                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = null,
                    modifier = Modifier
                        .width(100.dp).height(70.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Name
                Text(
                    text = name,
                    fontSize = 15.sp,
                    color = Color.Black
                )
            }
        }
    }
    override fun onResume() {
        super.onResume()
        shakeDetector.start()
    }

    override fun onPause() {
        super.onPause()
        shakeDetector.stop()
    }
}

