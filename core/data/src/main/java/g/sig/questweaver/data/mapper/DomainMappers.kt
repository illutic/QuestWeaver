@file:Suppress("TooManyFunctions")

package g.sig.questweaver.data.mapper

import g.sig.questweaver.data.dto.AnnotationDto
import g.sig.questweaver.data.dto.AnnotationDto.DrawingDto
import g.sig.questweaver.data.dto.AnnotationDto.TextDto
import g.sig.questweaver.data.dto.ColorDto
import g.sig.questweaver.data.dto.ConnectionStateDto
import g.sig.questweaver.data.dto.ConnectionStateDto.ConnectedDto
import g.sig.questweaver.data.dto.ConnectionStateDto.ConnectingDto
import g.sig.questweaver.data.dto.ConnectionStateDto.ErrorDto
import g.sig.questweaver.data.dto.ConnectionStateDto.FoundDto
import g.sig.questweaver.data.dto.ConnectionStateDto.IdleDto
import g.sig.questweaver.data.dto.ConnectionStateDto.LoadingDto
import g.sig.questweaver.data.dto.DeviceDto
import g.sig.questweaver.data.dto.Dto
import g.sig.questweaver.data.dto.FileDto
import g.sig.questweaver.data.dto.FileMetadataDto
import g.sig.questweaver.data.dto.GameDto
import g.sig.questweaver.data.dto.GameHomeStateDto
import g.sig.questweaver.data.dto.GameStateDto
import g.sig.questweaver.data.dto.PointDto
import g.sig.questweaver.data.dto.RemoveAnnotationDto
import g.sig.questweaver.data.dto.SizeDto
import g.sig.questweaver.data.dto.UserDto
import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.entities.blocks.Color
import g.sig.questweaver.domain.entities.blocks.Point
import g.sig.questweaver.domain.entities.blocks.Size
import g.sig.questweaver.domain.entities.blocks.Uri
import g.sig.questweaver.domain.entities.common.Annotation
import g.sig.questweaver.domain.entities.common.Device
import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.entities.common.RemoveAnnotation
import g.sig.questweaver.domain.entities.common.User
import g.sig.questweaver.domain.entities.io.File
import g.sig.questweaver.domain.entities.io.FileMetadata
import g.sig.questweaver.domain.entities.states.ConnectionState
import g.sig.questweaver.domain.entities.states.GameHomeState
import g.sig.questweaver.domain.entities.states.GameState
import android.net.Uri as AndroidUri

@Suppress("CyclomaticComplexMethod")
fun DomainEntity.toDto(): Dto = when (this) {
    is File -> toDto()
    is FileMetadata -> toDto()
    is Game -> toDto()
    is User -> toDto()
    is Color -> toDto()
    is Annotation.Drawing -> toDto()
    is Annotation.Text -> toDto()
    is Point -> toDto()
    is Size -> toDto()
    is Device -> toDto()
    is ConnectionState -> toDto()
    is GameHomeState -> toDto()
    is GameState -> toDto()
    is RemoveAnnotation -> toDto()
    else -> throw IllegalArgumentException("Unknown DomainEntity type: $this")
}

fun Uri.toDto(): AndroidUri = AndroidUri.parse(value)
fun File.toDto() = FileDto(uri.toDto(), metadata.toDto())
fun FileMetadata.toDto() = FileMetadataDto(name, extension)
fun Game.toDto() = GameDto(gameId, title, description, players, maxPlayers, dmId.orEmpty())
fun User.toDto() = UserDto(id = id, name = name)
fun Color.toDto() = ColorDto(value)
fun Point.toDto() = PointDto(x, y)
fun Size.toDto() = SizeDto(width, height)
fun Device.toDto() = DeviceDto(
    id = id,
    name = name,
    connectionState = connectionState.toDto(),
)

fun GameHomeState.toDto() = GameHomeStateDto(
    annotationDtos = annotations.map { it.toDto() },
    allowEditing = allowEditing,
)

fun GameState.toDto() = GameStateDto(
    game = game.toDto(),
    connectedUsers = connectedUsers.map { it.toDto() },
    gameHomeState = gameHomeState.toDto(),
)

fun Annotation.Drawing.toDto() = DrawingDto(
    id = id,
    createdBy = createdBy,
    strokeSize = strokeSize.toDto(),
    colorDto = color.toDto(),
    path = path.map { it.toDto() }
)

fun Annotation.Text.toDto() = TextDto(
    id = id,
    createdBy = createdBy,
    text = text,
    sizeDTO = size.toDto(),
    colorDTO = color.toDto(),
    anchor = anchor.toDto()
)

fun Annotation.Image.toDto() = AnnotationDto.ImageDto(
    id = id,
    createdBy = createdBy,
    size = size.toDto(),
    anchor = anchor.toDto(),
    fileDto = FileDto(uri.toDto(), metadata.toDto())
)

fun Annotation.toDto() = when (this) {
    is Annotation.Drawing -> toDto()
    is Annotation.Text -> toDto()
    is Annotation.Image -> toDto()
}

fun ConnectionState.toDto(): ConnectionStateDto =
    when (this) {
        is ConnectionState.Idle -> IdleDto
        is ConnectionState.Loading -> LoadingDto
        is ConnectionState.Connecting -> ConnectingDto(endpointId, name)
        is ConnectionState.Connected -> ConnectedDto(endpointId)
        is ConnectionState.Found -> FoundDto(endpointId, name)
        is ConnectionState.Error.GenericError -> ErrorDto.GenericError(endpointId, message)
        is ConnectionState.Error.RejectError -> ErrorDto.RejectError(endpointId)
        is ConnectionState.Error.LostError -> ErrorDto.LostError(endpointId)
        is ConnectionState.Error.FailureError -> ErrorDto.FailureError(exception)
        is ConnectionState.Error.ConnectionRequestError ->
            ErrorDto.ConnectionRequestError(throwable)

        is ConnectionState.Error.DisconnectionError ->
            ErrorDto.DisconnectionError(endpointId)
    }

fun RemoveAnnotation.toDto() = RemoveAnnotationDto(id)
