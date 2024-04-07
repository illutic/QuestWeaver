@file:UseSerializers(UriSerializer::class)

package g.sig.data.entities

import android.net.Uri
import g.sig.common.data.serializers.UriSerializer
import g.sig.data.utils.toDomain
import g.sig.data.utils.toUri
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
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
    val imageUri: Uri = Uri.EMPTY,
    val players: Int = 0,
    val maxPlayers: Int
) {
    fun toDomain() = GameDomain(id, title, description, imageUri.toDomain(), players, maxPlayers)

    companion object {
        val Empty = Game("", "", "", Uri.EMPTY, 0, 0)
        fun GameDomain.fromDomain() = Game(id, title, description, imageUri.toUri(), players, maxPlayers)
    }
}
