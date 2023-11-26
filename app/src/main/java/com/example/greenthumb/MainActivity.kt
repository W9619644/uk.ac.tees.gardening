package com.example.greenthumb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gardening.R
import com.example.greenthumb.common.navigateActivity
import com.example.greenthumb.views.login.LoginActivity
import com.example.greenthumb.views.login.LoginActivity2
import com.example.greenthumb.views.setting.ui.theme.GardeningTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GardeningTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Splash()
                }
            }
        }

        Timer().schedule(object : TimerTask() {
            override fun run() {
             navigateActivity(this@MainActivity, LoginActivity2::class.java)
             finish()
            }
        }, 2000)
    }

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
                    val x = centerX + (radius * Math.cos(angle)).toFloat()
                    val y = centerY + (radius * Math.sin(angle)).toFloat()

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
}