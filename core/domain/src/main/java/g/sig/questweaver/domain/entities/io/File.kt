package g.sig.questweaver.domain.entities.io

import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.entities.blocks.Uri

data class File(val uri: Uri, val metadata: FileMetadata) : DomainEntity
