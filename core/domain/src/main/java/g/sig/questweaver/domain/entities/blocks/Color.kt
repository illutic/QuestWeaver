package g.sig.questweaver.domain.entities.blocks

import g.sig.questweaver.domain.entities.DomainEntity

@JvmInline
value class Color(
    val value: ULong,
) : DomainEntity
