package g.sig.domain.entities

sealed interface NearbyAction {
    data class AddGame(val game: Game) : NearbyAction
    data class RemoveGame(val id: String) : NearbyAction
}