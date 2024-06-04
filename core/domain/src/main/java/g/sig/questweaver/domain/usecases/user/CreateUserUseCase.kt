package g.sig.questweaver.domain.usecases.user

import g.sig.questweaver.domain.entities.User
import g.sig.questweaver.domain.repositories.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.UUID

class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val mainDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(name: String) {
        withContext(mainDispatcher) {
            userRepository.saveUser(User(id = UUID.randomUUID().toString(), name = name))
        }
    }
}
