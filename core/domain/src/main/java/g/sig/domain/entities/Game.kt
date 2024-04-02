package g.sig.domain.entities

data class Game(
    val id: String,
    val title: String,
    val description: String,
    val imageUri: String,
    val players: Int,
    val maxPlayers: Int
)
