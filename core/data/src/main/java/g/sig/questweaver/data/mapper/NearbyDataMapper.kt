package g.sig.questweaver.data.mapper

import g.sig.questweaver.data.entities.DataEntity
import g.sig.questweaver.data.entities.File
import g.sig.questweaver.data.entities.FileMetadata
import g.sig.questweaver.data.entities.Game
import g.sig.questweaver.data.entities.Stream
import g.sig.questweaver.data.entities.User
import g.sig.questweaver.domain.entities.PayloadData

fun DataEntity.toDomainData(): PayloadData = when (this) {
    is File -> toDomain()
    is FileMetadata -> toDomain()
    is Game -> toDomain().copy(isDM = false)
    is User -> toDomain()
    is Stream -> toDomain()
}
