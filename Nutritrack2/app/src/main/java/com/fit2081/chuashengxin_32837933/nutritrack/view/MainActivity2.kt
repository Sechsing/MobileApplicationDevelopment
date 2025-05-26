package com.fit2081.chuashengxin_32837933.nutritrack.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.fit2081.chuashengxin_32837933.nutritrack.ui.theme.NutritrackTheme
import com.fit2081.chuashengxin_32837933.nutritrack.viewmodel.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

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
fun LoginScreen(viewModel: LoginViewModel = viewModel()) {
    val context = LocalContext.current

    val selectedId by viewModel.selectedId.collectAsState()
    val password by viewModel.password.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val userList by viewModel.userList.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            context.startActivity(Intent(context, MainActivity3::class.java))
        }
    }

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

            Spacer(modifier = Modifier.height(16.dp))

            IdDropdownMenu(
                ids = userList,
                selectedId = selectedId,
                onIdSelected = viewModel::updateSelectedId
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordBox(
                password = password,
                onPasswordChange = viewModel::updatePassword
            )

            Text(
                text = "This app is only for pre-registered users. Please enter\n" +
                        "your ID and password or Register to claim your\n" +
                        "account on your first visit.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                fontSize = 12.sp,
                color = Color.Gray
            )

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    viewModel.validateUser {
                        context.startActivity(Intent(context, MainActivity3::class.java))
                    }
                },
                shape = RoundedCornerShape(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA))
            ) {
                Text(text = "Continue", fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { context.startActivity(Intent(context, RegistrationActivity::class.java)) },
                shape = RoundedCornerShape(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA))
            ) {
                Text(text = "Register", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdDropdownMenu(
    ids: List<String>,
    selectedId: String,
    onIdSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedId,
            onValueChange = {},
            readOnly = true,
            label = { Text("My ID (provided by your clinician)") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        ExposedDropdownMenu(
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
fun PasswordBox(password: String, onPasswordChange: (String) -> Unit) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Password") },
        modifier = Modifier
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val icon = if (passwordVisible) Icons.Filled.Lock else Icons.Filled.Info
            val description = if (passwordVisible) "Hide password" else "Show password"
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = icon, contentDescription = description)
            }
        }
    )
}
