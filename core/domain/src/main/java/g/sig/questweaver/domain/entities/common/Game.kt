package g.sig.questweaver.domain.entities.common

import g.sig.questweaver.domain.entities.blocks.Uri
import g.sig.questweaver.domain.entities.io.PayloadData

data class Game(
    val gameId: String,
    val title: String,
    val description: String,
    val imageUri: Uri? = null,
    val players: Int = 0,
    val maxPlayers: Int,
    val dmId: String? = null,
) : PayloadData
