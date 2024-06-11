package g.sig.questweaver.data.entities.states

import g.sig.questweaver.data.entities.common.GameDto
import g.sig.questweaver.data.entities.common.GameDto.Companion.fromDomain
import g.sig.questweaver.data.entities.common.UserDto
import g.sig.questweaver.data.entities.common.UserDto.Companion.fromDomain
import g.sig.questweaver.data.entities.states.GameHomeStateDto.Companion.fromDomain
import g.sig.questweaver.domain.entities.states.GameState
import kotlinx.serialization.Serializable

@Serializable
data class GameStateDto(
    val game: GameDto,
    val connectedUsers: List<UserDto>,
    val gameHomeState: GameHomeStateDto
) {
    fun toDomain() = GameState(
        game = game.toDomain(),
        connectedUsers = connectedUsers.map { it.toDomain() },
        gameHomeState = gameHomeState.toDomain(),
    )

    companion object {
        fun GameState.fromDomain() = GameStateDto(
            game = game.fromDomain(),
            connectedUsers = connectedUsers.map { it.fromDomain() },
            gameHomeState = gameHomeState.fromDomain(),
        )
    }
}
