package kr.lightswitch.ui.login

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.lightswitch.LightSwitchApplication
import kr.lightswitch.model.response.LoginResponse
import kr.lightswitch.network.LightSwitchRepository
import kr.lightswitch.ui.MainViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val lightSwitchRepository: LightSwitchRepository,
) : MainViewModel() {

    private val _loginResponse = MutableStateFlow<LoginResponse?>(null)
    val loginResponse: StateFlow<LoginResponse?> = _loginResponse

    fun handleLogin(email: String, password: String) {
        viewModelScope.launch {
            lightSwitchRepository.login(
                email = email,
                password = password,
                onStart = {
                    Timber.d("onStart login")
                },
                onComplete = {
                    Timber.d("onComplete login")
                },
                onError = { error ->
                    Timber.d("error occured : $error")
                }
            ).collect {
                _loginResponse.value = it.data
                super.login(it.data)
            }
        }
    }

    override fun logout() {
        _loginResponse.value = null
        super.logout()
    }
}