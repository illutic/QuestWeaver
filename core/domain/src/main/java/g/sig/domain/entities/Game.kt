package g.sig.domain.entities

data class Game(
    val id: String,
    val title: String,
    val description: String,
    val imageUri: Uri? = null,
    val players: Int = 0,
    val maxPlayers: Int
)
