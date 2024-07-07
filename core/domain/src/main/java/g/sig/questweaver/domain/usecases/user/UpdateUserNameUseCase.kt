package g.sig.questweaver.domain.usecases.user

import g.sig.questweaver.domain.repositories.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateUserNameUseCase(
    private val userRepository: UserRepository,
    private val mainDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(name: String) {
        withContext(mainDispatcher) {
            val user = userRepository.getUser()
            userRepository.updateUser(user.copy(name = name))
        }
    }
}
