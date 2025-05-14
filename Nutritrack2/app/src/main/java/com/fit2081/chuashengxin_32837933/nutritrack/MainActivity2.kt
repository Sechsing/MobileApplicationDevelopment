package com.fit2081.chuashengxin_32837933.nutritrack

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.fit2081.chuashengxin_32837933.nutritrack.ui.theme.NutritrackTheme
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutritrackTheme {
                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("NutriTrackPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()

    var selectedId by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val userCredentials = remember { loadUserCredentials(context) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(192.dp))

            Text(text = "Log in", fontSize = 24.sp, color = Color.Black, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(64.dp))

            IdDropdownMenu(userCredentials.keys.toList(), selectedId, onIdSelected = { selectedId = it })

            PhoneNumberBox(phoneNumber, onPhoneNumberChange = { phoneNumber = it })

            Text(
                text = "This app is only for pre-registered users. Please have your ID and phone number handy before continuing.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(64.dp))

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (validateCredentials(selectedId, phoneNumber, userCredentials)) {
                        saveUserId(context, userId = selectedId)
                        context.startActivity(Intent(context, MainActivity3::class.java))
                    } else {
                        errorMessage = "Invalid ID or Phone Number"
                    }
                },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA))
            ) {
                Text(text = "Continue", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun IdDropdownMenu(ids: List<String>, selectedId: String, onIdSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = selectedId,
            onValueChange = {},
            readOnly = true,
            label = { Text("My ID (provided by your clinician)", fontWeight = FontWeight.Bold) },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select ID")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ids.forEach { id ->
                DropdownMenuItem(
                    text = { Text(id) },
                    onClick = {
                        onIdSelected(id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PhoneNumberBox(phoneNumber: String, onPhoneNumberChange: (String) -> Unit) {
    Box(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            label = { Text("Phone Number", fontWeight = FontWeight.Bold) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
    }
}

fun loadUserCredentials(context: Context): Map<String, String> {
    val credentials = mutableMapOf<String, String>()

    try {
        val inputStream = context.assets.open("validate.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.useLines { lines ->
            lines.drop(1).forEach { line -> // Skipping header
                val parts = line.split(",").map { it.trim() }
                val phone = parts[0]
                val id = parts[1]
                credentials[id] = phone
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return credentials
}

fun validateCredentials(id: String, phone: String, userCredentials: Map<String, String>): Boolean {
    return userCredentials[id] == phone
}

fun saveUserId(context: Context, userId: String) {
    val sharedPreferences = context.getSharedPreferences("NutriTrackPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("USER_ID", userId).apply()
}
