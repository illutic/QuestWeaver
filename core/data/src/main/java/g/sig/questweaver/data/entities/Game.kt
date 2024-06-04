@file:UseSerializers(UriSerializer::class)

package g.sig.questweaver.data.entities

import android.net.Uri
import g.sig.questweaver.common.data.serializers.UriSerializer
import g.sig.questweaver.data.utils.toDomain
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import g.sig.questweaver.domain.entities.Game as GameDomain

@Serializable
data class Game(
    val gameId: String,
    val title: String,
    val description: String,
    val players: Int = 0,
    val maxPlayers: Int,
    val isDM: Boolean = false
) : DataEntity {
    fun toDomain() =
        GameDomain(gameId, title, description, Uri.EMPTY.toDomain(), players, maxPlayers, isDM)

    companion object {
        val Empty = Game("", "", "", 0, 0)
        fun GameDomain.fromDomain() = Game(gameId, title, description, players, maxPlayers, isDM)
    }
}
