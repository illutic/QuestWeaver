package g.sig.questweaver.domain.usecases.host

class VerifyDescriptionUseCase {
    operator fun invoke(description: String): Boolean = description.isNotBlank()
}
