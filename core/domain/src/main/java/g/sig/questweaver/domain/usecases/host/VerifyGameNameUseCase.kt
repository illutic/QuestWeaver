package g.sig.questweaver.domain.usecases.host

class VerifyGameNameUseCase {
    operator fun invoke(name: String): Boolean = name.isNotBlank()
}
