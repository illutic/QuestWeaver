package g.sig.domain.usecases.user

import g.sig.domain.repositories.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class HasUserUseCase(
    private val userRepository: UserRepository,
    private val mainDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke() = withContext(mainDispatcher) { userRepository.hasUser() }
}
