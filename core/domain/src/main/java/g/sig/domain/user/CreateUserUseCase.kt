package g.sig.domain.user

import g.sig.data.entities.user.User
import g.sig.data.repositories.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(name: String) {
        withContext(mainDispatcher) {
            userRepository.saveUser(User(UUID.randomUUID(), name))
        }
    }
}