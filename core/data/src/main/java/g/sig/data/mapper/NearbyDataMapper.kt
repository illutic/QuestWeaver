package g.sig.data.mapper

import g.sig.data.entities.DataEntity
import g.sig.data.entities.File
import g.sig.data.entities.FileMetadata
import g.sig.data.entities.Game
import g.sig.data.entities.Stream
import g.sig.data.entities.User
import g.sig.domain.entities.PayloadData

fun DataEntity.toDomainData(): PayloadData = when (this) {
    is File -> toDomain()
    is FileMetadata -> toDomain()
    is Game -> toDomain()
    is User -> toDomain()
    is Stream -> toDomain()
}
