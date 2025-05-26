package com.fit2081.chuashengxin_32837933.nutritrack.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.fit2081.chuashengxin_32837933.nutritrack.ui.theme.NutritrackTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fit2081.chuashengxin_32837933.nutritrack.viewmodel.InsightsViewModel

class MainActivity5 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutritrackTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationBar(selectedIndex = 1) }
                ) { innerPadding ->
                    InsightsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun InsightsScreen(
    modifier: Modifier = Modifier,
    viewModel: InsightsViewModel = viewModel()
) {
    val context = LocalContext.current
    val scores by viewModel.heifaScores.collectAsState()
    val totalScore by viewModel.totalScore.collectAsState()

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Insights: Food Score",
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(8.dp))

        scores.forEach { (category, score) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1.5f)) {
                    Text(
                        text = category,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Slider(
                    value = score,
                    onValueChange = { },
                    valueRange = if (category in listOf("Grains & Cereals", "Whole Grains", "Water",
                            "Alcohol", "Saturated Fats", "Unsaturated Fats")) 0f..5f else 0f..10f,
                    modifier = Modifier.weight(3.5f).height(32.dp)
                )

                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (category in listOf("Grains & Cereals", "Whole Grains", "Water",
                                "Alcohol", "Saturated Fats", "Unsaturated Fats"))
                            "${"%.2f".format(score)}/5"
                        else
                            "${"%.2f".format(score)}/10",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Text(text = "Total Food Quality Score", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Slider(
                value = totalScore,
                onValueChange = { },
                valueRange = 0f..100f,
                modifier = Modifier.weight(3f)
            )
            Text(
                text = "${"%.0f".format(totalScore)}/100",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Button(
            onClick = {
                val shareText = "My Total Food Quality Score is ${"%.0f".format(totalScore)}/100. How does yours compare?"

                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share text via"))
            },
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
        ) {
            Icon(Icons.Outlined.Share, contentDescription = "Share", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Share with someone", color = Color.White)
        }

        Button(
            onClick = { context.startActivity(Intent(context, NutriCoachActivity::class.java)) },
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Icon(Icons.Outlined.Build, contentDescription = "Improve", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Improve my diet!", color = Color.White)
        }
    }
}

