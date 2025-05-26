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

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsersRepository

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _heifaScore = MutableStateFlow("N/A")
    val heifaScore: StateFlow<String> = _heifaScore

    init {
        val dao = UsersDatabase.getDatabase(application).userDao()
        repository = UsersRepository(dao)
    }

    fun loadUserData() {
        viewModelScope.launch {
            val sharedPrefs = getApplication<Application>().getSharedPreferences("NutriTrackPrefs", Context.MODE_PRIVATE)
            val idString = sharedPrefs.getString("USER_ID", "")
            val userId = idString?.toIntOrNull()

            if (userId != null) {
                val user = repository.getUserById(userId)
                if (user != null) {
                    _userName.value = user.name
                    _heifaScore.value = when (user.sex) {
                        "Male" -> user.heifaTotalScoreMale.toString()
                        "Female" -> user.heifaTotalScoreFemale.toString()
                        else -> "N/A"
                    }
                }
            }
        }
    }
}
