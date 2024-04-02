package g.sig.domain.repositories

import g.sig.domain.entities.Permission

interface PermissionsRepository {
    fun hasPermissions(vararg permission: Permission): Boolean
}