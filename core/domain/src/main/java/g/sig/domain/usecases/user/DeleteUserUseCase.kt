package g.sig.domain.usecases.user

import g.sig.domain.repositories.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteUserUseCase(
    private val userRepository: UserRepository,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke() {
        withContext(mainDispatcher) {
            userRepository.deleteUser()
        }
    }
}