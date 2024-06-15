package g.sig.questweaver.data.mapper

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
import g.sig.questweaver.data.dto.InteractionDto
import g.sig.questweaver.data.dto.PointDto
import g.sig.questweaver.data.dto.SizeDto
import g.sig.questweaver.data.dto.UserDto
import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.entities.blocks.Color
import g.sig.questweaver.domain.entities.blocks.Point
import g.sig.questweaver.domain.entities.blocks.Size
import g.sig.questweaver.domain.entities.blocks.Uri
import g.sig.questweaver.domain.entities.common.Device
import g.sig.questweaver.domain.entities.common.Game
import g.sig.questweaver.domain.entities.common.Interaction
import g.sig.questweaver.domain.entities.common.User
import g.sig.questweaver.domain.entities.io.File
import g.sig.questweaver.domain.entities.io.FileMetadata
import g.sig.questweaver.domain.entities.states.ConnectionState
import g.sig.questweaver.domain.entities.states.ConnectionState.Connected
import g.sig.questweaver.domain.entities.states.ConnectionState.Connecting
import g.sig.questweaver.domain.entities.states.ConnectionState.Error
import g.sig.questweaver.domain.entities.states.ConnectionState.Found
import g.sig.questweaver.domain.entities.states.ConnectionState.Idle
import g.sig.questweaver.domain.entities.states.ConnectionState.Loading
import g.sig.questweaver.domain.entities.states.GameHomeState
import g.sig.questweaver.domain.entities.states.GameState
import android.net.Uri as AndroidUri

fun Dto.toDomain(): DomainEntity = when (this) {
    is FileDto -> toDomain()
    is FileMetadataDto -> toDomain()
    is GameDto -> toDomain()
    is UserDto -> toDomain()
    is ColorDto -> toDomain()
    is InteractionDto.DrawingDto -> toDomain()
    is InteractionDto.TextDto -> toDomain()
    is PointDto -> toDomain()
    is SizeDto -> toDomain()
    else -> throw IllegalArgumentException("Unknown Dto type: $this")
}

fun AndroidUri.toDomain(): Uri = Uri(toString())
fun FileDto.toDomain() = File(uri.toDomain(), metadata.toDomain())
fun FileMetadataDto.toDomain() = FileMetadata(name, extension)
fun GameDto.toDomain() =
    Game(gameId, title, description, AndroidUri.EMPTY.toDomain(), players, maxPlayers, dmId)

fun UserDto.toDomain() = User(name, id.toString())
fun ColorDto.toDomain() = Color(value)
fun PointDto.toDomain() = Point(x, y)
fun SizeDto.toDomain() = Size(width, height)
fun DeviceDto.toDomain() = Device(
    id = id,
    name = name,
    connectionState = connectionState.toDomain(),
)

fun GameHomeStateDto.toDomain() = GameHomeState(
    interactions = interactions.map { it.toDomain() },
    allowEditing = allowEditing,
)

fun GameStateDto.toDomain() = GameState(
    game = game.toDomain(),
    connectedUsers = connectedUsers.map { it.toDomain() },
    gameHomeState = gameHomeState.toDomain(),
)

fun InteractionDto.DrawingDto.toDomain() = Interaction.Drawing(
    id = id,
    strokeWidth = strokeWidth,
    color = colorDto.toDomain(),
    path = path.map { it.toDomain() }
)

fun InteractionDto.TextDto.toDomain() = Interaction.Text(
    id = id,
    text = text,
    size = sizeDTO.toDomain(),
    color = colorDTO.toDomain(),
    anchor = anchor.toDomain()
)

fun InteractionDto.toDomain(): Interaction = when (this) {
    is InteractionDto.DrawingDto -> toDomain()
    is InteractionDto.TextDto -> toDomain()
}

fun ConnectionStateDto.toDomain(): ConnectionState =
    when (this) {
        is IdleDto -> Idle
        is LoadingDto -> Loading
        is ConnectingDto -> Connecting(endpointId, name)
        is ConnectedDto -> Connected(endpointId)
        is FoundDto -> Found(endpointId, name)
        is ErrorDto.GenericError -> Error.GenericError(endpointId, message)
        is ErrorDto.RejectError -> Error.RejectError(endpointId)
        is ErrorDto.LostError -> Error.LostError(endpointId)
        is ErrorDto.FailureError -> Error.FailureError(exception)
        is ErrorDto.ConnectionRequestError -> Error.ConnectionRequestError(throwable)
        is ErrorDto.DisconnectionError -> Error.DisconnectionError(endpointId)
    }
