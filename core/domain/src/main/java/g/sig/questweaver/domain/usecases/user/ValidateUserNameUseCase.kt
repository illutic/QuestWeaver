package g.sig.questweaver.domain.usecases.user

class ValidateUserNameUseCase {
    operator fun invoke(name: String) =
        when {
            name.trim().isEmpty() -> ValidationState.EmptyName
            else -> ValidationState.Valid
        }

    sealed interface ValidationState {
        data object EmptyName : ValidationState

        data object Valid : ValidationState
    }
}
