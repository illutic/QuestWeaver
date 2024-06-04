package g.sig.questweaver.domain.repositories

import g.sig.questweaver.domain.entities.Permission

interface PermissionsRepository {
    fun hasPermissions(permission: List<Permission>): Boolean
}
