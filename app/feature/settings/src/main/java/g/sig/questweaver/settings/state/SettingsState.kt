package g.sig.questweaver.settings.state

sealed interface SettingsState {
    data object Idle : SettingsState
    data object Loading : SettingsState
    data object Loaded : SettingsState
}
