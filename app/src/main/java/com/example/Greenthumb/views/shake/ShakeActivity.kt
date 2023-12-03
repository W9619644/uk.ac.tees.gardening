package com.example.greenthumb.views.shake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.gardening.R
import com.example.greenthumb.common.ShakeDetector

class ShakeActivity : AppCompatActivity() {

    private val imageUrls = listOf(
        "https://example.com/image1.jpg",
        "https://example.com/image2.jpg",
        "https://example.com/image3.jpg"
        // Add more image URLs as needed
    )

    private lateinit var shakeDetector: ShakeDetector
    private lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shake)
    }
}