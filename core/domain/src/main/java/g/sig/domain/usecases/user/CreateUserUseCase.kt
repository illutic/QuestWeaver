package g.sig.domain.usecases.user

import g.sig.domain.entities.User
import g.sig.domain.repositories.UserRepository
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
            userRepository.saveUser(User(id = UUID.randomUUID().toString(), name = name))
        }
    }
}