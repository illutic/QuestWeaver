package g.sig.questweaver.settings.state

sealed interface SettingsIntent {
    data object Back : SettingsIntent

    data object OpenPrivacyPolicy : SettingsIntent
}
