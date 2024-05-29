package g.sig.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.domain.entities.RemoteConfigKeys
import g.sig.domain.usecases.remoteconfig.FetchRemoteConfigValueUseCase
import g.sig.settings.state.SettingsEvent
import g.sig.settings.state.SettingsIntent
import g.sig.settings.state.SettingsState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val fetchRemoteConfigValue: FetchRemoteConfigValueUseCase
) : ViewModel() {
    private val _events = Channel<SettingsEvent>()
    private val _state = MutableStateFlow<SettingsState>(SettingsState.Idle)
    val state = _state.asStateFlow()
    val events = _events.receiveAsFlow()

    fun handleIntent(intent: SettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                SettingsIntent.Back -> _events.send(SettingsEvent.Back)
                SettingsIntent.OpenPrivacyPolicy -> {
                    _state.value = SettingsState.Loading
                    val url = fetchRemoteConfigValue(RemoteConfigKeys.PRIVACY_POLICY_URL_KEY)
                    _events.send(SettingsEvent.OpenPrivacyPolicy(url))
                    _state.value = SettingsState.Loaded
                }
            }
        }
    }
}