package com.fit2081.chuashengxin_32837933.nutritrack.view

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fit2081.chuashengxin_32837933.nutritrack.viewmodel.SettingsViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.fit2081.chuashengxin_32837933.nutritrack.ui.theme.NutritrackTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutritrackTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationBar(selectedIndex = 3) }
                ) { innerPadding ->
                    SettingsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(),
) {
    val context = LocalContext.current
    val name by viewModel.userName.observeAsState(initial = "Loading...")
    val phone by viewModel.userPhone.observeAsState(initial = "Loading...")
    val id by viewModel.userId.observeAsState(initial = "Loading...")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        Text("ACCOUNT", style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(16.dp))

        AccountInfoRow(icon = Icons.Default.Person, info = name)
        Spacer(Modifier.height(8.dp))

        AccountInfoRow(icon = Icons.Default.Phone, info = phone)
        Spacer(Modifier.height(8.dp))

        AccountInfoRow(icon = Icons.Default.Edit, info = id)
        Spacer(Modifier.height(8.dp))

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text("OTHER SETTINGS", style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(16.dp))

        SettingsItem(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            text = "Logout",
            onClick = {
                viewModel.logout()
                context.startActivity(Intent(context, MainActivity2::class.java))
            }
        )

        Spacer(Modifier.height(8.dp))

        SettingsItem(
            icon = Icons.Default.Person,
            text = "Clinician Login",
            onClick = { context.startActivity(Intent(context, ClinicianActivity::class.java)) }
        )
    }
}

@Composable
fun AccountInfoRow(icon: ImageVector, info: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(12.dp))
        Text(info, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun SettingsItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(12.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Go", modifier = Modifier.size(20.dp))
    }
}



