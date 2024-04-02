package g.sig.data.entities

import kotlinx.serialization.Serializable
import g.sig.domain.entities.Game as GameDomain

@Serializable
data class Games(val games: List<Game>) {
    fun toDomain() = games.map { it.toDomain() }
}

@Serializable
data class Game(
    val id: String,
    val title: String,
    val description: String,
    val imageUri: String,
    val players: Int,
    val maxPlayers: Int
) {
    fun toDomain() = GameDomain(id, title, description, imageUri, players, maxPlayers)

    companion object {
        fun GameDomain.fromDomain() = Game(id, title, description, imageUri, players, maxPlayers)
    }
}
