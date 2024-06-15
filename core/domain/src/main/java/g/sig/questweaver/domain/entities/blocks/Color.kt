package g.sig.questweaver.domain.entities.blocks

import g.sig.questweaver.domain.entities.DomainEntity

@JvmInline
value class Color(val value: UInt) : DomainEntity {
    init {
        require(value in 0u..0xFFFFFFFFu) {
            "Color value must be in [0, 0xFFFFFFFF], but was $value"
        }
    }
}
