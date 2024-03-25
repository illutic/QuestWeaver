package g.sig.domain.usecases.permissions

import g.sig.domain.entities.Permission

class GetNearbyPermissionUseCase(
    private vararg val permissions: Permission
) {
    operator fun invoke(): List<Permission> = permissions.toList()
}