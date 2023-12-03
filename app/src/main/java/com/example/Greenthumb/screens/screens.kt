package com.example.greenthumb.screens

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.gardening.R
import java.lang.Math.cos
import java.lang.Math.sin


class DashboardActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlantTrackerActivity()
            }
        }
    //Login
    @Composable
    fun LoginScreen() {
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
                    value = "",
                    onValueChange = {},
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
                    value = "",
                    onValueChange = {},
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
                    onClick = { /* Handle login button click */ },
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
                    .fillMaxWidth(),
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

    //Splash
    @Composable
    fun Splash() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PentagonShape(
                modifier = Modifier
                    .align(Alignment.Start)
                //.padding(end = 8.dp) // Adjust the padding as needed
            )

            Text(
                text = "GARDENING",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(android.graphics.Color.parseColor("#378805")),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            PentagonShape(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(start = 8.dp) // Adjust the padding as needed
            )
        }
    }

    @Composable
    fun PentagonShape(modifier: Modifier = Modifier) {
        Canvas(
            modifier = modifier
                .size(130.dp, 160.dp),
            //.padding(10.dp),
            onDraw = {
                val shapePath = Path()

                val centerX = size.width / 2
                val centerY = size.height / 2
                val radius = size.width / 2

                for (i in 0..4) {
                    val angle = Math.toRadians((i * 72).toDouble())
                    val x = centerX + (radius * cos(angle)).toFloat()
                    val y = centerY + (radius * sin(angle)).toFloat()

                    if (i == 0) {
                        shapePath.moveTo(x, y)
                    } else {
                        shapePath.lineTo(x, y)
                    }
                }

                drawPath(
                    path = shapePath,
                    color = Color(android.graphics.Color.parseColor("#378805")),
                    style = Fill
                )
            }
        )
    }

    //Plant Tracker ctivity
    @Composable
    fun PlantTrackerActivity() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(android.graphics.Color.parseColor("#BEBEBE")))
                .padding(5.dp)
        ) {
            // TextView for "Upload Data"
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                Text(
                    text = "Upload Data",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            // FloatingActionButton for Upload
            FloatingActionButton(
                onClick = { /* Handle upload button click */ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "Upload",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

}



