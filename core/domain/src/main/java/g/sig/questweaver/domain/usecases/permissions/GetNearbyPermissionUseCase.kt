package g.sig.questweaver.domain.usecases.permissions

import g.sig.questweaver.domain.entities.Permission

class GetNearbyPermissionUseCase(
    private val permissions: List<Permission>
) {
    operator fun invoke(): List<Permission> = permissions
}
