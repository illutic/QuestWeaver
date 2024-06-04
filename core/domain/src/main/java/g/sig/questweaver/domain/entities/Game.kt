package g.sig.questweaver.domain.entities

data class Game(
    val gameId: String,
    val title: String,
    val description: String,
    val imageUri: Uri? = null,
    val players: Int = 0,
    val maxPlayers: Int,
    val isDM: Boolean = false
) : PayloadData
