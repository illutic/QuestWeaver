package g.sig.domain.usecases.user

import g.sig.domain.repositories.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateUserNameUseCase(
    private val userRepository: UserRepository,
    private val mainDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(name: String) {
        withContext(mainDispatcher) {
            val user = userRepository.getUser()
            userRepository.updateUser(user.copy(name = name))
        }
    }
}