package com.example.greenthumb.views.tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gardening.R
import com.example.greenthumb.views.login.ui.theme.GardeningTheme

class PlantTracker2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GardeningTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    PlantListScreen()
                }
            }
        }
    }

    fun getDummyCartItems(): List<CartItem> {
        return listOf(
            CartItem(name = "Plant name", year = "2023", rank = "species"),
            CartItem(name = "Plant name", year = "2023", rank = "species"),
            CartItem(name = "Plant name", year = "2023",rank = "species")
        )
    }


    @Composable
    fun CartItemCard(cartItem: CartItem) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            shape = RoundedCornerShape(8.dp),
            backgroundColor = Color(android.graphics.Color.parseColor("#ddfdca")),
            elevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Product Image (Replace R.drawable.pizza with the actual resource ID for the image)
                Image(
                    painter = painterResource(id = R.drawable.pizza),
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(30.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Product Details
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    // Name of the product
                    Text(text = "name: ${cartItem.name}", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))

                    // Price of the product
                    Text(text = "Price: ${cartItem.year}", fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = "rank: ${cartItem.rank}", fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    // You can add more details or customize the text here
                    Text(text = "Some additional details", fontSize = 12.sp)
                }

                // Counter Section
            }
        }
    }
    @Composable
    fun PlantListScreen() {
        LazyColumn(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
        ) {
            items(getDummyCartItems()) { cartItem ->
                CartItemCard(cartItem = cartItem)
            }

            // Add some spacing between the LazyColumn and the TotalAmountButton
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }

        }
    }

    data class CartItem(val name: String, val year: String, val rank : String)

}