package g.sig.questweaver.domain.usecases.permissions

import g.sig.questweaver.domain.entities.Permission
import g.sig.questweaver.domain.repositories.PermissionsRepository

class HasPermissionsUseCase(private val repository: PermissionsRepository) {

    operator fun invoke(permissions: List<Permission>): Boolean =
        repository.hasPermissions(permissions)

    operator fun invoke(vararg permission: Permission): Boolean =
        repository.hasPermissions(listOf(*permission))
}
