package g.sig.questweaver.domain.entities

sealed interface NearbyAction {
    data class AddDevice(val device: Device) : NearbyAction
    data class RemoveDevice(val id: String) : NearbyAction
}
