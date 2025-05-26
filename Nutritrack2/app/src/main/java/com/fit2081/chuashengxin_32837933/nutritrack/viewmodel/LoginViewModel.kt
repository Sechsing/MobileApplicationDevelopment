package com.fit2081.chuashengxin_32837933.nutritrack.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.chuashengxin_32837933.nutritrack.data.UsersDatabase
import com.fit2081.chuashengxin_32837933.nutritrack.data.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsersRepository

    private val _selectedId = MutableStateFlow("")
    val selectedId: StateFlow<String> = _selectedId

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _userList = MutableStateFlow<List<String>>(emptyList())
    val userList: StateFlow<List<String>> = _userList

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val prefs = application.getSharedPreferences("NutriTrackPrefs", Context.MODE_PRIVATE)

    init {
        val dao = UsersDatabase.getDatabase(application).userDao()
        repository = UsersRepository(dao)

        repository.getAllUserIds()
            .onEach { ids -> _userList.value = ids }
            .launchIn(viewModelScope)

        checkLoginStatus()
    }

    private fun saveUserId(userId: String) {
        prefs.edit().putString("USER_ID", userId).apply()
        _isLoggedIn.value = true
    }

    private fun checkLoginStatus() {
        val userId = prefs.getString("USER_ID", null)
        if (userId != null) {
            _isLoggedIn.value = true
        }
    }

    fun updateSelectedId(id: String) {
        _selectedId.value = id
    }

    fun updatePassword(password: String) {
        _password.value = password
    }

    fun validateUser(onSuccess: () -> Unit) {
        val id = _selectedId.value.trim()
        val password = _password.value.trim()

        if (id.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Please enter ID and password"
            return
        }

        viewModelScope.launch {
            val user = repository.authenticateUser(id.toInt(), password)
            withContext(Dispatchers.Main) {
                if (user != null) {
                    saveUserId(id)
                    _errorMessage.value = ""
                    onSuccess()
                } else {
                    _errorMessage.value = "Invalid ID or password"
                }
            }
        }
    }
}
