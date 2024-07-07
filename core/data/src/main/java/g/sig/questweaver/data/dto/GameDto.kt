@file:UseSerializers(UriSerializer::class)

package g.sig.questweaver.data.dto

import g.sig.questweaver.common.data.serializers.UriSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class GameDto(
    val gameId: String,
    val title: String,
    val description: String,
    val players: Int = 0,
    val maxPlayers: Int = 0,
    val dmId: String = "",
    val hostDeviceId: String = "",
) : Dto
