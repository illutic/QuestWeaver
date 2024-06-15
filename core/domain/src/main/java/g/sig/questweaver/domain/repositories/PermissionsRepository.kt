package g.sig.questweaver.domain.repositories

import g.sig.questweaver.domain.entities.common.Permission

interface PermissionsRepository {
    fun hasPermissions(permission: List<Permission>): Boolean
}
