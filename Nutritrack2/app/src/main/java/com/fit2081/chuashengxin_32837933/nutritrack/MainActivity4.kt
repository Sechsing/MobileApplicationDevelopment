package com.fit2081.chuashengxin_32837933.nutritrack

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit2081.chuashengxin_32837933.nutritrack.ui.theme.NutritrackTheme
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity4 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutritrackTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationBar() }
                ) { innerPadding ->
                    HomeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("NutriTrackPrefs", Context.MODE_PRIVATE)
    val userID = sharedPreferences.getString("USER_ID", "")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Hello,", fontSize = 20.sp)
                Text(text = "User ${userID ?: ""}", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text(text = "You've already filled in your Food Intake Questionnaire.", fontSize = 14.sp)
            }
            IconButton(onClick = { context.startActivity(Intent(context, MainActivity3::class.java)) }) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.food_image), // Change to your actual image resource
            contentDescription = "Food Image",
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "My Score", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { context.startActivity(Intent(context, MainActivity5::class.java))}
                    ) {
                        Text(
                            text = "See all scores",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "See all scores",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Your Food Quality Score", fontSize = 16.sp)
                    Text(
                        text = readHEIFAScore(context, userID),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "What is the Food Quality Score?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your Food Quality Score provides a snapshot of how well your eating patterns align with established food guidelines, helping you identify both strengths and opportunities for improvement in your diet.\n\n" +
                    "This personalized measurement considers various food groups including vegetables, fruits, whole grains, and proteins to give you practical insights for making healthier food choices.",
            fontSize = 14.sp
        )
    }
}

@SuppressLint("SuspiciousIndentation")
fun readHEIFAScore(context: Context, userID: String?): String {
    try {
        val inputStream = context.assets.open("validate.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val lines = reader.readLines().drop(1)

        for (line in lines) {
            val values = line.split(",")
                if (userID == values[1]) {
                    val gender = values[2]
                    return if (gender == "Male") values[3] else values[4]
                }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "N/A"
}

@Composable
fun BottomNavigationBar() {
    val context = LocalContext.current
    var selectedIndex by remember { mutableIntStateOf(-1) }

    NavigationBar {
        NavigationBarItem(
            selected = selectedIndex == 0,
            onClick = {
                selectedIndex = 0
                context.startActivity(Intent(context, MainActivity4::class.java))
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = {
                selectedIndex = 1
                context.startActivity(Intent(context, MainActivity5::class.java))
            },
            icon = { Icon(Icons.Default.Info, contentDescription = "Insights") },
            label = { Text("Insights") }
        )
        NavigationBarItem(
            selected = selectedIndex == 2,
            onClick = { selectedIndex = 2 },
            icon = { Icon(Icons.Default.Star, contentDescription = "NutriCoach") },
            label = { Text("NutriCoach") }
        )
        NavigationBarItem(
            selected = selectedIndex == 3,
            onClick = { selectedIndex = 3 },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") }
        )
    }
}

