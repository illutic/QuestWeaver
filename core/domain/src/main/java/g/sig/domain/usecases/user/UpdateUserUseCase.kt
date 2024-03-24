package g.sig.domain.usecases.user

import g.sig.domain.repositories.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateUserUseCase(
    private val userRepository: UserRepository,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(name: String) {
        withContext(mainDispatcher) {
            val user = userRepository.getUser()
            userRepository.updateUser(user.copy(name = name))
        }
    }
}