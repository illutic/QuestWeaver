@file:UseSerializers(UriSerializer::class)

package g.sig.questweaver.data.entities.common

import android.net.Uri
import g.sig.questweaver.common.data.serializers.UriSerializer
import g.sig.questweaver.data.entities.Dto
import g.sig.questweaver.data.utils.toDomain
import g.sig.questweaver.domain.entities.common.Game
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class GameDto(
    val gameId: String,
    val title: String,
    val description: String,
    val players: Int = 0,
    val maxPlayers: Int,
    val dmId: String? = null,
) : Dto {
    fun toDomain() =
        Game(gameId, title, description, Uri.EMPTY.toDomain(), players, maxPlayers, dmId)

    companion object {
        val Empty = GameDto("", "", "", 0, 0)
        fun Game.fromDomain() = GameDto(gameId, title, description, players, maxPlayers, dmId)
    }
}
