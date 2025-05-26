package com.fit2081.chuashengxin_32837933.nutritrack.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fit2081.chuashengxin_32837933.nutritrack.data.FruityViceRetrofitInstance
import com.fit2081.chuashengxin_32837933.nutritrack.data.AIMessagesRepository
import com.fit2081.chuashengxin_32837933.nutritrack.data.UiState
import com.fit2081.chuashengxin_32837933.nutritrack.data.UsersRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NutriCoachViewModel(
    application: Application,
    private val messagesRepository: AIMessagesRepository,
    private val userRepository: UsersRepository
) : AndroidViewModel(application) {

    private val _showFruitSection = MutableStateFlow(false)
    val showFruitSection: StateFlow<Boolean> = _showFruitSection.asStateFlow()

    private val _nutritionFacts = MutableStateFlow<Map<String, String>>(emptyMap())
    val nutritionFacts: StateFlow<Map<String, String>> = _nutritionFacts.asStateFlow()

    private val _allMotivationalMessages = MutableStateFlow<List<String>>(emptyList())
    val allMotivationalMessages: StateFlow<List<String>> = _allMotivationalMessages.asStateFlow()

    private val _motivationalMessageUiState = MutableStateFlow<UiState>(UiState.Initial)
    val motivationalMessageUiState: StateFlow<UiState> = _motivationalMessageUiState.asStateFlow()

    init {
        loadLatestMotivationalMessage()
        loadAllMotivationalMessages()
        checkFruitEligibility()
    }

    class NutriCoachViewModelFactory(
        private val application: Application,
        private val messagesRepository: AIMessagesRepository,
        private val userRepository: UsersRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NutriCoachViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NutriCoachViewModel(application, messagesRepository, userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private fun checkFruitEligibility() {
        viewModelScope.launch {
            val sharedPrefs = getApplication<Application>().getSharedPreferences("NutriTrackPrefs", Context.MODE_PRIVATE)
            val userId = sharedPrefs.getString("USER_ID", "")?.toIntOrNull() ?: return@launch

            val user = userRepository.getUserById(userId)
            if (user != null) {
                val eligible = user.fruitServeSize < 2 || user.fruitVariationsScore < 2
                _showFruitSection.value = eligible
            }
        }
    }

    fun loadFruitData(fruit: String) {
        viewModelScope.launch {
            val response = FruityViceRetrofitInstance.api.getFruitByName(fruit.lowercase())
            _nutritionFacts.value = mapOf(
                "family" to response.family,
                "calories" to response.nutritions.calories.toString(),
                "fat" to response.nutritions.fat.toString(),
                "sugar" to response.nutritions.sugar.toString(),
                "carbohydrates" to response.nutritions.carbohydrates.toString(),
                "protein" to response.nutritions.protein.toString()
            )
        }
    }

    fun generateMotivationalTip() {
        viewModelScope.launch {
            _motivationalMessageUiState.value = UiState.Loading
            val prompt = "Generate a short encouraging message to help someone improve their fruit intake. Be creative."
            try {
                val output = messagesRepository.sendPrompt(prompt)
                messagesRepository.insertMessage(output)
                _motivationalMessageUiState.value = UiState.Success(output)
                loadAllMotivationalMessages()
            } catch (e: Exception) {
                _motivationalMessageUiState.value = UiState.Error("Failed to generate motivational tip.")
            }
        }
    }

    private fun loadAllMotivationalMessages() {
        viewModelScope.launch {
            try {
                val messages = messagesRepository.getAllMessages()
                _allMotivationalMessages.value = messages.map { it.messageText }
            } catch (e: Exception) {
                _allMotivationalMessages.value = emptyList()
            }
        }
    }

    private fun loadLatestMotivationalMessage() {
        viewModelScope.launch {
            try {
                val messages = messagesRepository.getAllMessages()
                _motivationalMessageUiState.value = UiState.Success(messages.lastOrNull()?.messageText ?: "No motivational tips yet.")
            } catch (e: Exception) {
                _motivationalMessageUiState.value = UiState.Error("No motivational tip has been generated.")
            }
        }
    }
}
