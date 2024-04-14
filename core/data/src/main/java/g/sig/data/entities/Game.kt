@file:UseSerializers(UriSerializer::class)

package g.sig.data.entities

import android.net.Uri
import g.sig.common.data.serializers.UriSerializer
import g.sig.data.utils.toDomain
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import g.sig.domain.entities.Game as GameDomain

@Serializable
data class Game(
    val gameId: String,
    val title: String,
    val description: String,
    val players: Int = 0,
    val maxPlayers: Int
) : DataEntity {
    fun toDomain() = GameDomain(gameId, title, description, Uri.EMPTY.toDomain(), players, maxPlayers)

    companion object {
        val Empty = Game("", "", "", 0, 0)
        fun GameDomain.fromDomain() = Game(gameId, title, description, players, maxPlayers)
    }
}
