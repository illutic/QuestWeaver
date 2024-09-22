package g.sig.questweaver.domain.entities.common

import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.entities.blocks.Uri

data class Game(
    val gameId: String,
    val title: String,
    val description: String,
    val imageUri: Uri? = null,
    val players: Int = 0,
    val maxPlayers: Int,
    val dmId: String? = null,
    val hostDeviceId: String? = null,
) : DomainEntity {
    companion object {
        val Empty =
            Game(
                gameId = "",
                title = "",
                description = "",
                maxPlayers = 0,
            )
    }
}
