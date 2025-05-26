package com.fit2081.chuashengxin_32837933.nutritrack.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class WelcomeViewModel : ViewModel() {
    var welcomeText = mutableStateOf("NutriTrack")
        private set
}