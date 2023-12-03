package com.example.greenthumb.views.login


import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp
import com.example.gardening.R
import com.example.gardening.databinding.ActivityLoginBinding
import com.example.greenthumb.common.isInternetConnected
import com.example.greenthumb.common.navigateActivity
import com.example.greenthumb.views.home.HomeActivity
import com.example.greenthumb.views.login.ui.theme.GardeningTheme

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.greenthumb.common.isInternetConnected
import com.example.greenthumb.common.navigateActivity

import com.example.greenthumb.views.register.RegisterActivity
import com.example.greenthumb.views.register.RegisterActivity2
import com.google.firebase.auth.FirebaseAuth

class LoginActivity2 : ComponentActivity() {
    lateinit var binding : ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    lateinit var dialog : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GardeningTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LoginScreen2()
                }
            }
        }
        getPostNotificationPermission()

//        loginViewModel.isLoading.observe(this@LoginActivity2 , Observer {
//            if(it){
//                dialog.setMessage("Loading....")
//                dialog?.show()
//            }
//            else{
//                if(dialog?.isShowing == true){
//                    dialog.cancel()
//                }
//            }
//        })

        // viewmodel using live data observer

        loginViewModel.isSuccess.observe(this@LoginActivity2 , Observer {
            if(it){
                navigateActivity(this@LoginActivity2, HomeActivity::class.java)
                // finish()
            }
        })

        loginViewModel.message.observe(this@LoginActivity2 , Observer {
            Toast.makeText(this@LoginActivity2 , ""+it, Toast.LENGTH_SHORT).show()
        })

        loginViewModel.emailmessage.observe(this@LoginActivity2 , Observer {
            binding.idUsernameLayout.error="Enter Email ID"
            binding.idUsernameLayout.isErrorEnabled = true
        })
        loginViewModel.passmessage.observe(this@LoginActivity2 , Observer {
            binding.idPasswordLayout0.error="Enter valid Password"
            binding.idPasswordLayout0.isErrorEnabled = true

        })


    }
    private fun getPostNotificationPermission() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
                requestPermissions(permissions, 1111)
            }
            else {
                // repeat the permission or open app details
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val user= FirebaseAuth.getInstance().currentUser
        if(user!=null){
            navigateActivity(this@LoginActivity2, HomeActivity::class.java)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1111 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  openImage()
                } else {
                    Toast.makeText(this@LoginActivity2, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }






    @Composable
    fun LoginScreen2() {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pentagon),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .padding(40.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Welcome",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "Log in or create your account",
                    fontSize = 10.sp,
                    color = Color(0xFF727272),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Username TextInputLayout
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = "Username",
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(18.dp))
                )

                // Password TextInputLayout
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.lock),
                            contentDescription = "Password",
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(18.dp))
                )

                // Spacer to create space between the password field and the button
                Spacer(modifier = Modifier.height(16.dp))

                // Button aligned to the right
                Button(
                    onClick = {
                        if(isInternetConnected(this@LoginActivity2)){
                            loginViewModel.authenticate(username,password)
                        }else{
                            Toast.makeText(this@LoginActivity2,"Please check internet connection",
                                Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .width(90.dp).align(Alignment.End)
                        .height(60.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(android.graphics.Color.parseColor("#378805")))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fastforward),
                        contentDescription = "Login",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth().clickable {
                        navigateActivity(this@LoginActivity2, RegisterActivity2::class.java)
                    },
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account?",
                    color = Color(0xFF252525),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(8.dp)
                )

                Text(
                    text = "Sign Up",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 2.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                )
            }
        }
    }

}
