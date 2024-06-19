package g.sig.questweaver.domain.entities.common

import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.entities.blocks.Color
import g.sig.questweaver.domain.entities.blocks.Point
import g.sig.questweaver.domain.entities.blocks.Size
import g.sig.questweaver.domain.entities.blocks.Uri
import g.sig.questweaver.domain.entities.io.FileMetadata

sealed interface Annotation : DomainEntity {
    val id: String
    val createdBy: String

    data class Drawing(
        val strokeSize: Size,
        val color: Color,
        val path: List<Point>,
        override val createdBy: String,
        override val id: String,
    ) : Annotation

    data class Text(
        val text: String,
        val size: Size,
        val color: Color,
        val anchor: Point,
        override val createdBy: String,
        override val id: String
    ) : Annotation

    data class Image(
        val uri: Uri,
        val metadata: FileMetadata,
        val size: Size,
        val anchor: Point,
        override val createdBy: String,
        override val id: String
    ) : Annotation
}
