package com.fit2081.chuashengxin_32837933.nutritrack

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class FoodIntakeViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("NutriTrackPrefs", Context.MODE_PRIVATE)

    val foodCategories = listOf("Fruits", "Vegetables", "Grains", "Red Meat", "Seafood", "Poultry", "Fish", "Eggs", "Nuts/Seeds")
    val selectedCategories = mutableStateMapOf<String, Boolean>()

    val personas = listOf("Health Devotee", "Mindful Eater", "Wellness Striver", "Balance Seeker", "Health Procrastinator", "Food Carefree")
    val selectedPersona = mutableStateOf<String?>(null)

    val biggestMealTime = mutableStateOf("00:00")
    val sleepTime = mutableStateOf("00:00")
    val wakeUpTime = mutableStateOf("00:00")

    init {
        loadData()
    }

    private fun loadData() {
        val savedSelections = sharedPreferences.getString("foodSelections", "")?.split(",") ?: listOf()
        foodCategories.forEach { category ->
            selectedCategories[category] = savedSelections.contains(category)
        }

        selectedPersona.value = sharedPreferences.getString("persona", null)
        biggestMealTime.value = sharedPreferences.getString("mealTime", "00:00") ?: "00:00"
        sleepTime.value = sharedPreferences.getString("sleepTime", "00:00") ?: "00:00"
        wakeUpTime.value = sharedPreferences.getString("wakeUpTime", "00:00") ?: "00:00"
    }

    fun saveData() {
        val selectedFoods = selectedCategories.filter { it.value }.keys.joinToString(",")
        with(sharedPreferences.edit()) {
            putString("foodSelections", selectedFoods)
            putString("persona", selectedPersona.value)
            putString("mealTime", biggestMealTime.value)
            putString("sleepTime", sleepTime.value)
            putString("wakeUpTime", wakeUpTime.value)
            apply()
        }
    }
}
