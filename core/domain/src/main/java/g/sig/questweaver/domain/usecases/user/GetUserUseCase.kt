package g.sig.questweaver.domain.usecases.user

import g.sig.questweaver.domain.repositories.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetUserUseCase(
    private val userRepository: UserRepository,
    private val mainDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke() = withContext(mainDispatcher) { userRepository.getUser() }
}
