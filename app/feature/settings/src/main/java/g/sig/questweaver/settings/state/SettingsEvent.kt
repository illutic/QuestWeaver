package g.sig.questweaver.settings.state

sealed interface SettingsEvent {
    data object Back : SettingsEvent

    data class OpenPrivacyPolicy(
        val url: String,
    ) : SettingsEvent
}
