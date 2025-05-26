package com.fit2081.chuashengxin_32837933.nutritrack.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.alpha
import com.fit2081.chuashengxin_32837933.nutritrack.data.AIMessagesDatabase
import com.fit2081.chuashengxin_32837933.nutritrack.data.AIMessagesRepository
import com.fit2081.chuashengxin_32837933.nutritrack.data.UiState
import com.fit2081.chuashengxin_32837933.nutritrack.data.UsersDatabase
import com.fit2081.chuashengxin_32837933.nutritrack.data.UsersRepository
import com.fit2081.chuashengxin_32837933.nutritrack.ui.theme.NutritrackTheme
import com.fit2081.chuashengxin_32837933.nutritrack.viewmodel.NutriCoachViewModel

class NutriCoachActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val messageDao = AIMessagesDatabase.getDatabase(applicationContext).messagesDao()
        val messageRepo = AIMessagesRepository(messageDao)

        val userDao = UsersDatabase.getDatabase(applicationContext).userDao()
        val userRepo = UsersRepository(userDao)

        val factory = NutriCoachViewModel.NutriCoachViewModelFactory(application, messageRepo, userRepo)
        val viewModel = ViewModelProvider(this, factory)[NutriCoachViewModel::class.java]

        setContent {
            NutritrackTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationBar(selectedIndex = 2) }
                ) { innerPadding ->
                    NutriCoachScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun NutriCoachScreen(
    modifier: Modifier = Modifier,
    viewModel: NutriCoachViewModel = viewModel()
) {
    val showFruitSection by viewModel.showFruitSection.collectAsState()
    val motivationalUiState by viewModel.motivationalMessageUiState.collectAsState()
    val nutritionFacts by viewModel.nutritionFacts.collectAsState()
    val allTips by viewModel.allMotivationalMessages.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showTipsDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "NutriCoach",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (showFruitSection) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Fruit Name",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Search for fruits...") },
                            shape = MaterialTheme.shapes.medium,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                disabledContainerColor = MaterialTheme.colorScheme.surface,
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (searchQuery.isNotBlank()) {
                                    viewModel.loadFruitData(searchQuery)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                            modifier = Modifier
                                .width(120.dp)
                                .height(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Search")
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 8.dp)
                    ) {
                        nutritionFacts.forEach { (key, value) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                            ) {
                                Text(
                                    text = key,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = ": $value",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                thickness = 1.dp
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Button(
                    onClick = { viewModel.generateMotivationalTip() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Motivational",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Motivational Message (AI)")
                }

                Spacer(modifier = Modifier.height(8.dp))

                when (motivationalUiState) {
                    is UiState.Initial -> {}
                    is UiState.Loading -> {
                        FlashingLoadingAnimation()
                    }

                    is UiState.Success -> {
                        Text(
                            text = (motivationalUiState as UiState.Success).outputText,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    is UiState.Error -> {
                        Text(
                            text = (motivationalUiState as UiState.Error).errorMessage,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Red
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    thickness = 1.dp
                )

                Button(
                    onClick = { showTipsDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = "All Tips",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Show All Tips")
                }
            }
        }

        if (showTipsDialog) {
            AlertDialog(
                onDismissRequest = { showTipsDialog = false },
                title = {
                    Text(
                        text = "AI Tips",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    if (allTips.isEmpty()) {
                        Text(
                            "No motivational tips available.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn {
                            items(allTips) { tip ->
                                Text(
                                    text = tip,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )
                                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showTipsDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                    ) {
                        Text("Done", color = Color.White)
                    }
                }
            )
        }
    }
}

    @Composable
fun BottomNavigationBar(selectedIndex: Int) {
    val context = LocalContext.current

    NavigationBar {
        NavigationBarItem(
            selected = selectedIndex == 0,
            onClick = {
                context.startActivity(Intent(context, MainActivity4::class.java))
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = {
                context.startActivity(Intent(context, MainActivity5::class.java))
            },
            icon = { Icon(Icons.Default.Info, contentDescription = "Insights") },
            label = { Text("Insights") }
        )
        NavigationBarItem(
            selected = selectedIndex == 2,
            onClick = {
                context.startActivity(Intent(context, NutriCoachActivity::class.java))
            },
            icon = { Icon(Icons.Default.Star, contentDescription = "NutriCoach") },
            label = { Text("NutriCoach") }
        )
        NavigationBarItem(
            selected = selectedIndex == 3,
            onClick = {
                context.startActivity(Intent(context, SettingsActivity::class.java))
            },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") }
        )
    }
}

@Composable
fun FlashingLoadingAnimation() {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Loading...",
            modifier = Modifier.alpha(alpha),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
