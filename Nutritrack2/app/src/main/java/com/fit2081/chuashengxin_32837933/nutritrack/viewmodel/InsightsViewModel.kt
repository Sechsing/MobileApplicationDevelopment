package com.fit2081.chuashengxin_32837933.nutritrack.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.chuashengxin_32837933.nutritrack.data.UsersDatabase
import com.fit2081.chuashengxin_32837933.nutritrack.data.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InsightsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsersRepository
    private val _userID = MutableStateFlow("")

    private val _heifaScores = MutableStateFlow<Map<String, Float>>(emptyMap())
    val heifaScores: StateFlow<Map<String, Float>> = _heifaScores

    private val _totalScore = MutableStateFlow(0f)
    val totalScore: StateFlow<Float> = _totalScore

    init {
        val dao = UsersDatabase.getDatabase(application).userDao()
        repository = UsersRepository(dao)
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val sharedPrefs = getApplication<Application>().getSharedPreferences("NutriTrackPrefs", Context.MODE_PRIVATE)
            val userId = sharedPrefs.getString("USER_ID", "")?.toIntOrNull() ?: return@launch
            _userID.value = userId.toString()

            val user = repository.getUserById(userId) ?: return@launch
            val isMale = user.sex.equals("Male", ignoreCase = true)

            _totalScore.value = if (isMale) user.heifaTotalScoreMale else user.heifaTotalScoreFemale

            val scores = mapOf(
                "Vegetables" to if (isMale) user.vegetablesHeifaScoreMale else user.vegetablesHeifaScoreFemale,
                "Fruits" to if (isMale) user.fruitHeifaScoreMale else user.fruitHeifaScoreFemale,
                "Grains & Cereals" to if (isMale) user.grainsAndCerealsHeifaScoreMale else user.grainsAndCerealsHeifaScoreFemale,
                "Whole Grains" to if (isMale) user.wholeGrainsHeifaScoreMale else user.wholeGrainsHeifaScoreFemale,
                "Meat & Alternatives" to if (isMale) user.meatAndAlternativesHeifaScoreMale else user.meatAndAlternativesHeifaScoreFemale,
                "Dairy" to if (isMale) user.dairyAndAlternativesHeifaScoreMale else user.dairyAndAlternativesHeifaScoreFemale,
                "Water" to if (isMale) user.waterHeifaScoreMale else user.waterHeifaScoreFemale,
                "Sodium" to if (isMale) user.sodiumHeifaScoreMale else user.sodiumHeifaScoreFemale,
                "Alcohol" to if (isMale) user.alcoholHeifaScoreMale else user.alcoholHeifaScoreFemale,
                "Sugar" to if (isMale) user.sugarHeifaScoreMale else user.sugarHeifaScoreFemale,
                "Saturated Fats" to if (isMale) user.saturatedFatHeifaScoreMale else user.saturatedFatHeifaScoreFemale,
                "Unsaturated Fats" to if (isMale) user.unsaturatedFatHeifaScoreMale else user.unsaturatedFatHeifaScoreFemale,
                "Discretionary Foods" to if (isMale) user.discretionaryHeifaScoreMale else user.discretionaryHeifaScoreFemale,
            )

            _heifaScores.value = scores
        }
    }
}
