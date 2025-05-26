package com.fit2081.chuashengxin_32837933.nutritrack.view

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.fit2081.chuashengxin_32837933.nutritrack.ui.theme.NutritrackTheme
import androidx.activity.viewModels
import com.fit2081.chuashengxin_32837933.nutritrack.FoodIntakeViewModel
import com.fit2081.chuashengxin_32837933.nutritrack.R

class MainActivity3 : ComponentActivity() {

    private val viewModel: FoodIntakeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutritrackTheme {
                FoodIntakeScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FoodIntakeScreen(viewModel: FoodIntakeViewModel) {
    val context = LocalContext.current
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val foodCategories = viewModel.foodCategories
    val selectedCategories = viewModel.selectedCategories
    val personas = viewModel.personas
    val selectedPersona = viewModel.selectedPersona
    val biggestMealTime = viewModel.biggestMealTime
    val sleepTime = viewModel.sleepTime
    val wakeUpTime = viewModel.wakeUpTime

    var dropdownExpanded by remember { mutableStateOf(false) }
    var modalPersona by remember { mutableStateOf<String?>(null) }
    var showPersonaModal by remember { mutableStateOf(false) }

    val personaDetails = mapOf(
        "Health Devotee" to Pair(R.drawable.persona_1, "I’m passionate about healthy eating & health plays a big part in my life. I use social media to follow active lifestyle personalities or get new recipes/exercise ideas. I may even buy superfoods or follow a particular type of diet. I like to think I am super healthy."),
        "Mindful Eater" to Pair(R.drawable.persona_2, "I’m health-conscious and being healthy and eating healthy is important to me. Although health means different things to different people, I make conscious lifestyle decisions about eating based on what I believe healthy means. I look for new recipes and healthy eating information on social media."),
        "Wellness Striver" to Pair(R.drawable.persona_3, "I aspire to be healthy (but struggle sometimes). Healthy eating is hard work! I’ve tried to improve my diet, but always find things that make it difficult to stick with the changes. Sometimes I notice recipe ideas or healthy eating hacks, and if it seems easy enough, I’ll give it a go."),
        "Balance Seeker" to Pair(R.drawable.persona_4, "I try and live a balanced lifestyle, and I think that all foods are okay in moderation. I shouldn’t have to feel guilty about eating a piece of cake now and again. I get all sorts of inspiration from social media like finding out about new restaurants, fun recipes and sometimes healthy eating tips."),
        "Health Procrastinator" to Pair(R.drawable.persona_5, "I’m contemplating healthy eating but it’s not a priority for me right now. I know the basics about what it means to be healthy, but it doesn’t seem relevant to me right now. I have taken a few steps to be healthier but I am not motivated to make it a high priority because I have too many other things going on in my life."),
        "Food Carefree" to Pair(R.drawable.persona_6, "I’m not bothered about healthy eating. I don’t really see the point and I don’t think about it. I don’t really notice healthy eating tips or recipes and I don’t care what I eat.")
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Food Intake Questionnaire", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPressedDispatcher?.onBackPressed()
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Tick all the food categories you can eat:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(foodCategories) { category ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedCategories[category] = !(selectedCategories[category] ?: false)
                            }
                    ) {
                        Checkbox(
                            checked = selectedCategories[category] ?: false,
                            onCheckedChange = { selectedCategories[category] = it }
                        )
                        Text(category, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text("Your Persona", fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(4.dp))

            Text("People can be broadly classified into 6 different types based on their eating preferences. Click on each button below to find out the different types, and select the type that best fits you!", fontSize = 12.sp)

            Spacer(modifier = Modifier.height(4.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
            ) {
                personas.forEach { persona ->
                    TextButton(
                        onClick = {
                            modalPersona = persona
                            showPersonaModal = true
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = Color.Blue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(persona, fontSize = 12.sp)
                    }
                }
            }

            if (showPersonaModal && modalPersona != null) {
                PersonaModalDialog(
                    personaName = modalPersona!!,
                    personaDescription = personaDetails[modalPersona]?.second ?: "No description available",
                    personaImageRes = personaDetails[modalPersona]?.first ?: R.drawable.nutritrack_logo,
                    onDismiss = { showPersonaModal = false }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Which persona best fits you?", fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.padding(4.dp))

            Box {
                OutlinedTextField(
                    value = selectedPersona.value ?: ("Select Option"),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown Icon",
                            modifier = Modifier.clickable { dropdownExpanded = !dropdownExpanded })
                    }
                )
                DropdownMenu(expanded = dropdownExpanded, onDismissRequest = { dropdownExpanded = false }) {
                    personas.forEach { persona ->
                        DropdownMenuItem(
                            text = { Text(persona) },
                            onClick = {
                                selectedPersona.value = persona
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column {
                Text(
                    text = "Timings",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                )

                TimePickerRow("What time of day approx. do you normally eat your biggest meal?", biggestMealTime)
                TimePickerRow("What time of day approx. do you go to sleep at night?", sleepTime)
                TimePickerRow("What time of day approx. do you wake up in the morning?", wakeUpTime)
            }

            Button(
                onClick = {
                    viewModel.saveData()  // Save via ViewModel
                    context.startActivity(Intent(context, MainActivity4::class.java))
                },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA))
            ) {
                Text(text = "Save", fontSize = 18.sp, color = Color.White)
            }

        }
    }
}

@Composable
fun PersonaModalDialog(
    personaName: String,
    personaDescription: String,
    personaImageRes: Int,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = personaName, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = personaImageRes),
                    contentDescription = "Persona Image",
                    modifier = Modifier.size(100.dp)
                )
                Text(text = personaDescription, textAlign = TextAlign.Center)
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun TimePickerRow(question: String, selectedTime: MutableState<String>) {
    var showTimePicker by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = question,
            modifier = Modifier.weight(1.5f),
            fontSize = 14.sp
        )

        OutlinedTextField(
            value = selectedTime.value,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .weight(1f)
                .clickable { showTimePicker = true },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select Time",
                    modifier = Modifier.clickable { showTimePicker = true }
                )
            }
        )
    }

    if (showTimePicker) {
        TimePickerFun(selectedTime)
        showTimePicker = false
    }
}

@Composable
fun TimePickerFun(mTime: MutableState<String>) {
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar.get(Calendar.HOUR_OF_DAY)
    val mMinute = mCalendar.get(Calendar.MINUTE)

    TimePickerDialog(
        mContext,
        { _, selectedHour: Int, selectedMinute: Int ->
            mTime.value = "$selectedHour:$selectedMinute"
        },
        mHour, mMinute, false
    ).show()
}