package g.sig.questweaver.domain.entities.blocks

import g.sig.questweaver.domain.entities.io.PayloadData

@JvmInline
value class Color(val value: UInt) : PayloadData {
    init {
        require(value in 0u..0xFFFFFFFFu) {
            "Color value must be in [0, 0xFFFFFFFF], but was $value"
        }
    }
}
