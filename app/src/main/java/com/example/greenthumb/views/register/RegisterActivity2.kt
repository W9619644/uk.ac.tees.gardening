package com.example.greenthumb.views.register

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.lifecycle.Observer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gardening.R
import com.example.greenthumb.common.isInternetConnected
import com.example.greenthumb.common.navigateActivity
import com.example.greenthumb.models.RegisterModel
import com.example.greenthumb.views.home.HomeActivity2
import com.example.greenthumb.views.login.LoginActivity2
import com.example.greenthumb.views.register.ui.theme.GardeningTheme

class RegisterActivity2 : ComponentActivity() {
    private val registerViewModel: RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GardeningTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RegistrationScreen()
                }
            }
        }


        registerViewModel.isSuccess.observe(this@RegisterActivity2 , Observer {
            navigateActivity(this@RegisterActivity2, HomeActivity2::class.java)
            //finish()
        })

        registerViewModel.message.observe(this@RegisterActivity2 , Observer {
            Toast.makeText(this@RegisterActivity2 , ""+it, Toast.LENGTH_SHORT).show()
        })
    }
    @Composable
    fun RegistrationScreen() {

        var fullName by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White)
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
                    text = "Create Account",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "Sign up to get started",
                    fontSize = 10.sp,
                    color = Color(0xFF727272),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Full Name TextInputLayout
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = "Full Name",
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(Color.White, shape = RoundedCornerShape(18.dp))
                )

                // Email TextInputLayout
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.email),
                            contentDescription = "Email",
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(Color.White, shape = RoundedCornerShape(18.dp))
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.phone),
                            contentDescription = "Password",
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth()
                        .height(60.dp)
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
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(Color.White, shape = RoundedCornerShape(18.dp))
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.lock),
                            contentDescription = "Password",
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(Color.White, shape = RoundedCornerShape(18.dp))
                )

                // Spacer to create space between the password field and the button
                Spacer(modifier = Modifier.height(16.dp))

                // Button aligned to the right
                Button(
                    onClick = {
                        val model = RegisterModel(email,fullName,password,"",phone)
                        if(isInternetConnected(this@RegisterActivity2)){
                            Log.e("manan","hellow wordl"+model.toString())
                            registerViewModel.register(model ,confirmPassword,this@RegisterActivity2)
                        }else{
                            Toast.makeText(this@RegisterActivity2,"Please check internet connection",Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(android.graphics.Color.parseColor("#378805")))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fastforward),
                        contentDescription = "Sign Up",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth()
                    .clickable {
                        navigateActivity(this@RegisterActivity2, LoginActivity2::class.java)
                    },
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account?",
                    color = Color(0xFF252525),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(8.dp)
                )

                Text(
                    text = "Log In",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 2.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                )
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
        RegistrationScreen()
    }
}
