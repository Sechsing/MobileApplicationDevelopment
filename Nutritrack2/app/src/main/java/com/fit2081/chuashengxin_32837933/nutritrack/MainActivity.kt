package com.fit2081.chuashengxin_32837933.nutritrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fit2081.chuashengxin_32837933.nutritrack.ui.theme.NutritrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutritrackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WelcomeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(128.dp))

            Image(
                painter = painterResource(id = R.drawable.nutritrack_logo), // Replace with actual logo
                contentDescription = "NutriTrack Logo",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "NutriTrack",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "This app provides general health and nutrition information for educational purposes only. It is not intended as medical advice, diagnosis, or treatment. Always consult a qualified healthcare professional before making any changes to your diet, exercise, or health regimen.\n\n" +
                        "Use this app at your own risk.\n\n" +
                        "If you’d like to see an Accredited Practicing Dietitian (APD), please visit Monash Nutrition/Dietetics Clinic: \nhttps://www.monash.edu/medicine/scs/nutrition/clinics/nutrition",
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val intent = Intent(context, MainActivity2::class.java)
                    context.startActivity(intent) // Launch MainActivity2
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA)) // Purple color
            ) {
                Text(text = "Login", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(128.dp))

            Text(
                text = "Designed  by Chua Sheng Xin (32837933)",
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
