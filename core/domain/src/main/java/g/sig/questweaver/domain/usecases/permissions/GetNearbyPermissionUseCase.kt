package g.sig.questweaver.domain.usecases.permissions

import g.sig.questweaver.domain.entities.common.Permission

class GetNearbyPermissionUseCase(
    private val permissions: List<Permission>
) {
    operator fun invoke(): List<Permission> = permissions
}
