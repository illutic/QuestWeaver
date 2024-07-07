package g.sig.questweaver.domain.entities.io

import g.sig.questweaver.domain.entities.DomainEntity

data class FileMetadata(
    val name: String,
    val extension: String,
) : DomainEntity
