package com.fit2081.chuashengxin_32837933.nutritrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClinicianViewModel : ViewModel() {
    private val _clinicianKey = MutableStateFlow("")
    val clinicianKey: StateFlow<String> = _clinicianKey.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val passKey = "dollar-entry-apples"

    fun updateClinicianKey(key: String) {
        viewModelScope.launch {
            _clinicianKey.value = key
        }
    }

    fun validateClinicianKey() {
        viewModelScope.launch {
            if (_clinicianKey.value == passKey) {
                _loginSuccess.value = true
            } else {
                _loginSuccess.value = false
                _errorMessage.value = "Invalid key. Please try again."
            }
        }
    }
}