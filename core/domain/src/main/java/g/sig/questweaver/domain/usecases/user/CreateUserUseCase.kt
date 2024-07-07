package g.sig.questweaver.domain.usecases.user

import g.sig.questweaver.domain.entities.common.User
import g.sig.questweaver.domain.repositories.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val mainDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(
        id: String,
        name: String,
    ) {
        withContext(mainDispatcher) {
            userRepository.saveUser(User(id = id, name = name))
        }
    }
}
