package g.sig.questweaver.domain.entities.blocks

import g.sig.questweaver.domain.entities.io.PayloadData

data class Size(val width: Float, val height: Float) : PayloadData {
    init {
        require(width in 0f..1f) { "width must be in [0,1], but was $width" }
        require(height in 0f..1f) { "height must be in [0,1], but was $height" }
    }
}
