package com.fit2081.chuashengxin_32837933.nutritrack.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fit2081.chuashengxin_32837933.nutritrack.data.*
import com.google.gson.Gson
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ClinicianDashboardViewModel(
    private val usersRepository: UsersRepository,
    private val messagesRepository: AIMessagesRepository
) : ViewModel() {

    private val _maleAverage = MutableStateFlow(0f)
    val maleAverage: StateFlow<Float> = _maleAverage.asStateFlow()

    private val _femaleAverage = MutableStateFlow(0f)
    val femaleAverage: StateFlow<Float> = _femaleAverage.asStateFlow()

    private val _aiInsights = MutableStateFlow("")
    val aiInsights: StateFlow<String> = _aiInsights.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        refreshData()
    }

    class ClinicianDashboardViewModelFactory(
        private val usersRepository: UsersRepository,
        private val messagesRepository: AIMessagesRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ClinicianDashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ClinicianDashboardViewModel(usersRepository, messagesRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            usersRepository.getAllUsers().collect { users ->
                val maleScores = users.filter { it.sex.equals("Male", ignoreCase = true) }
                    .map { it.heifaTotalScoreMale }

                val femaleScores = users.filter { it.sex.equals("Female", ignoreCase = true) }
                    .map { it.heifaTotalScoreFemale }

                _maleAverage.value = maleScores.takeIf { it.isNotEmpty() }?.average()?.toFloat() ?: 0f
                _femaleAverage.value = femaleScores.takeIf { it.isNotEmpty() }?.average()?.toFloat() ?: 0f
            }
        }
    }

    private fun buildClinicalPrompt(users: List<User>): String {
        val gson = Gson()

        val limitedUsers = if (users.size > 50) users.take(50) else users

        data class SimplifiedUser(
            val userId: Int,
            val sex: String,
            val heifaTotalScore: Float,
            val fruitServeSize: Float,
            val fruitVariationsScore: Float,
            val vegetablesServeSize: Float,
            val vegetablesVariationsScore: Float,
            val grainsServeSize: Float,
            val wholeGrainsServeSize: Float,
            val water: Float,
            val sugar: Float,
            val saturatedFat: Float,
            val alcoholDrinks: Float,
            val sodiumMg: Float,
            val discretionaryServeSize: Float
        )

        val simplifiedUsers = limitedUsers.map {
            SimplifiedUser(
                userId = it.userId,
                sex = it.sex,
                heifaTotalScore = if (it.sex == "Male") it.heifaTotalScoreMale else it.heifaTotalScoreFemale,
                fruitServeSize = it.fruitServeSize,
                fruitVariationsScore = it.fruitVariationsScore,
                vegetablesServeSize = it.vegetablesWithLegumesAllocatedServeSize,
                vegetablesVariationsScore = it.vegetablesVariationsScore,
                grainsServeSize = it.grainsAndCerealsServeSize,
                wholeGrainsServeSize = it.wholeGrainsServeSize,
                water = it.water,
                sugar = it.sugar,
                saturatedFat = it.saturatedFat,
                alcoholDrinks = it.alcoholStandardDrinks,
                sodiumMg = it.sodiumMgMilligrams,
                discretionaryServeSize = it.discretionaryServeSize
            )
        }

        val fields = listOf(
            "userId",
            "sex",
            "heifaTotalScore",
            "fruitServeSize",
            "fruitVariationsScore",
            "vegetablesServeSize",
            "vegetablesVariationsScore",
            "grainsServeSize",
            "wholeGrainsServeSize",
            "water",
            "sugar",
            "saturatedFat",
            "alcoholDrinks",
            "sodiumMg",
            "discretionaryServeSize"
        )

        val values = simplifiedUsers.map {
            listOf(
                it.userId,
                it.sex,
                it.heifaTotalScore,
                it.fruitServeSize,
                it.fruitVariationsScore,
                it.vegetablesServeSize,
                it.vegetablesVariationsScore,
                it.grainsServeSize,
                it.wholeGrainsServeSize,
                it.water,
                it.sugar,
                it.saturatedFat,
                it.alcoholDrinks,
                it.sodiumMg,
                it.discretionaryServeSize
            )
        }

        val fieldsJson = gson.toJson(fields)
        val valuesJson = gson.toJson(values)

        val prompt = """
        You are a clinical data analyst AI.

        Below is nutritional user data represented in a compact format:

        Fields:
        $fieldsJson

        Data rows (each corresponds to a user, values in the same order as fields):
        $valuesJson

        Please identify and describe three significant data patterns, such as:
        - Relationships between nutrient intakes (e.g., water, wholegrains, fruits),
        - Gender-based differences in HEIFA scores,
        - Any other meaningful trend or anomaly.

        Simply provide these three significant data patterns and elaborate about them
        in three paragraphs, no other clarification or response is needed
        
        For example, one paragraph can look like this:
        "Variable Water Intake: Consumption of water varies greatly
        among the users in this dataset, with scores ranging from 0 to
        100. There isn't a clear, immediate correlation in this small sample
        between water intake score and the overall HEIFA score, though
        some high scorers did have high water intake."
    """.trimIndent()

        return prompt
    }

    fun generateClinicalDataInsight() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val users = usersRepository.getAllUsers().first()

                if (users.isEmpty()) {
                    _aiInsights.value = "No user data available."
                    _uiState.value = UiState.Success("No data to analyze.")
                    return@launch
                }

                val prompt = buildClinicalPrompt(users)

                Log.d("ClinicianPrompt", prompt)

                val response = messagesRepository.sendPrompt(prompt)

                _uiState.value = UiState.Success("Insight generated.")
                _aiInsights.value = response

            } catch (e: Exception) {
                Log.e("InsightError", "Exception type: ${e::class.java.name}")
                Log.e("InsightError", "Exception message: ${e.message}")
                Log.e("InsightError", Log.getStackTraceString(e))

                _uiState.value = UiState.Error("Insight generation failed: ${e.message}")
            }
        }
    }
}
