package g.sig.questweaver.domain.usecases.user

import g.sig.questweaver.domain.entities.common.User
import g.sig.questweaver.domain.repositories.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetUserUseCase(
    private val userRepository: UserRepository,
    private val mainDispatcher: CoroutineDispatcher,
) {
    private var user: User = User.Empty

    suspend operator fun invoke() =
        withContext(mainDispatcher) {
            if (user == User.Empty) {
                user = userRepository.getUser()
            }
            user
        }
}
