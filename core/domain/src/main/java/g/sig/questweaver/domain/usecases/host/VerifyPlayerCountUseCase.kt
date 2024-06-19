package g.sig.questweaver.domain.usecases.host

class VerifyPlayerCountUseCase {
    operator fun invoke(players: Int?): Boolean = players in MIN_PLAYERS..MAX_PLAYERS

    companion object {
        const val MAX_PLAYERS = 8
        const val MIN_PLAYERS = 2
    }
}
