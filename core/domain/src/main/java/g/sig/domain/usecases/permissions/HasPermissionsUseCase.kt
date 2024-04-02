package g.sig.domain.usecases.permissions

import g.sig.domain.entities.Permission
import g.sig.domain.repositories.PermissionsRepository

class HasPermissionsUseCase(private val repository: PermissionsRepository) {

    operator fun invoke(vararg permission: Permission): Boolean = repository.hasPermissions(*permission)
}