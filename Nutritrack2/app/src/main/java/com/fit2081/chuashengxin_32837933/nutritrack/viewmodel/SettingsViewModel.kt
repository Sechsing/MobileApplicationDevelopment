package com.fit2081.chuashengxin_32837933.nutritrack.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fit2081.chuashengxin_32837933.nutritrack.data.UsersDatabase
import com.fit2081.chuashengxin_32837933.nutritrack.data.UsersRepository
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsersRepository

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _userPhone = MutableLiveData<String>()
    val userPhone: LiveData<String> = _userPhone

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> = _userId

    init {
        val dao = UsersDatabase.getDatabase(application).userDao()
        repository = UsersRepository(dao)
        loadUserDetails()
    }

    private fun loadUserDetails() {
        viewModelScope.launch {
            val sharedPrefs = getApplication<Application>().getSharedPreferences("NutriTrackPrefs", Context.MODE_PRIVATE)
            val idString = sharedPrefs.getString("USER_ID", "")
            val userIdInt = idString?.toIntOrNull()

            if (userIdInt != null) {
                val user = repository.getUserById(userIdInt)
                if (user != null) {
                    _userName.postValue(user.name)
                    _userPhone.postValue(user.phoneNumber)
                    _userId.postValue(user.userId.toString())
                } else {
                    _userName.postValue("N/A")
                    _userPhone.postValue("N/A")
                    _userId.postValue("N/A")
                }
            }
        }
    }

    fun logout() {
        val sharedPrefs = getApplication<Application>().getSharedPreferences("NutriTrackPrefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().clear().apply()
    }
}
