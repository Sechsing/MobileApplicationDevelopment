package com.fit2081.chuashengxin_32837933.nutritrack.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.fit2081.chuashengxin_32837933.nutritrack.data.*
import com.fit2081.chuashengxin_32837933.nutritrack.ui.theme.NutritrackTheme
import com.fit2081.chuashengxin_32837933.nutritrack.viewmodel.ClinicianDashboardViewModel

class ClinicianDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userDao = UsersDatabase.getDatabase(applicationContext).userDao()
        val usersRepository = UsersRepository(userDao)

        val aiMessagesDao = AIMessagesDatabase.getDatabase(applicationContext).messagesDao()
        val messagesRepository = AIMessagesRepository(aiMessagesDao)

        val factory = ClinicianDashboardViewModel.ClinicianDashboardViewModelFactory(
            usersRepository = usersRepository,
            messagesRepository = messagesRepository
        )

        val viewModel = ViewModelProvider(this, factory)[ClinicianDashboardViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            NutritrackTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationBar(selectedIndex = -1) }
                ) { innerPadding ->
                    ClinicianDashboardScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                        onDoneClick = {
                            val intent = Intent(this, SettingsActivity::class.java)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ClinicianDashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: ClinicianDashboardViewModel,
    onDoneClick: () -> Unit
) {
    val maleAverage by viewModel.maleAverage.collectAsState()
    val femaleAverage by viewModel.femaleAverage.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val aiInsights by viewModel.aiInsights.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshData()
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Clinician Dashboard",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Average HEIFA (Male)",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = maleAverage.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column {
                        Text(
                            text = "Average HEIFA (Female)",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = femaleAverage.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 24.dp),
                    thickness = 1.dp
                )

                Button(
                    onClick = { viewModel.generateClinicalDataInsight() },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Find Data Pattern")
                }
            }

            item {
                when (uiState) {
                    is UiState.Loading -> {
                        FlashingLoadingAnimation()
                    }
                    is UiState.Error -> {
                        Text(
                            text = "Error: ${(uiState as UiState.Error).errorMessage}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                    is UiState.Success -> {
                        if (aiInsights.isNotBlank()) {
                            AIInsightsCardList(insights = aiInsights)
                        }
                    }
                    else -> Unit
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Button(
                onClick = onDoneClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
            ) {
                Text("Done")
            }
        }
    }
}

@Composable
fun AIInsightsCardList(insights: String) {
    val paragraphs = insights
        .split("\n\n")
        .filter { it.isNotBlank() }

    Column(modifier = Modifier.padding(16.dp)) {
        paragraphs.forEach { paragraph ->
            val titleAndBody = paragraph.split(":", limit = 2)
            val title = titleAndBody.getOrNull(0)?.trim() ?: ""
            val body = titleAndBody.getOrNull(1)?.trim() ?: ""

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = body,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

