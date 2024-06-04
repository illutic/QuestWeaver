package g.sig.questweaver.domain.usecases.host

class VerifyPlayerCountUseCase {
    operator fun invoke(players: Int): Boolean = players in 1..8
}
