package com.fit2081.chuashengxin_32837933.nutritrack.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.chuashengxin_32837933.nutritrack.data.UsersDatabase
import com.fit2081.chuashengxin_32837933.nutritrack.data.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UsersRepository

    private val _userList = MutableStateFlow<List<String>>(emptyList())
    val userList: StateFlow<List<String>> = _userList

    private val _selectedUserId = MutableStateFlow("")
    val selectedUserId: StateFlow<String> = _selectedUserId

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun updateSelectedUserId(id: String) {
        _selectedUserId.value = id
    }

    fun updatePhoneNumber(value: String) {
        _phoneNumber.value = value
    }

    fun updateName(value: String) {
        _name.value = value
    }

    fun updatePassword(value: String) {
        _password.value = value
    }

    fun updateConfirmPassword(value: String) {
        _confirmPassword.value = value
    }

    init {
        val dao = UsersDatabase.getDatabase(application).userDao()
        repository = UsersRepository(dao)
        loadUsersWithEmptyPassword()
    }

    private fun loadUsersWithEmptyPassword() {
        viewModelScope.launch {
            repository.getAllUsers().collect { users ->
                val eligibleIds = users.filter { it.password.isEmpty() }.map { it.userId.toString() }
                _userList.value = eligibleIds
            }
        }
    }

    fun registerUser(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val id = _selectedUserId.value.toIntOrNull()
            val phone = _phoneNumber.value
            val updatedName = _name.value
            val updatedPassword = _password.value
            val confirmPass = _confirmPassword.value

            if (id == null) {
                _errorMessage.value = "Please select a valid user ID."
                return@launch
            }

            if (phone.isEmpty()) {
                _errorMessage.value = "Please enter your phone number."
                return@launch
            }

            if (updatedPassword.isEmpty() || confirmPass.isEmpty()) {
                _errorMessage.value = "Please enter and confirm your password."
                return@launch
            }

            if (updatedPassword != confirmPass) {
                _errorMessage.value = "Passwords do not match."
                return@launch
            }

            val existingUser = repository.getUserById(id)
            if (existingUser == null) {
                _errorMessage.value = "User ID does not exist."
                return@launch
            }

            if (existingUser.password.isNotEmpty()) {
                _errorMessage.value = "This user ID is already registered."
                return@launch
            }

            if (existingUser.phoneNumber != phone) {
                _errorMessage.value = "Phone number does not match the registered ID."
                return@launch
            }

            val updatedUser = existingUser.copy(name = updatedName, password = updatedPassword)
            repository.updateUser(updatedUser)

            _errorMessage.value = ""
            onSuccess()
        }
    }
}
