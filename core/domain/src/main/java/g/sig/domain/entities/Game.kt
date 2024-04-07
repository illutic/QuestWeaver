package g.sig.domain.entities

data class Game(
    val id: String,
    val title: String,
    val description: String,
    val imageUri: Uri,
    val players: Int,
    val maxPlayers: Int
)
