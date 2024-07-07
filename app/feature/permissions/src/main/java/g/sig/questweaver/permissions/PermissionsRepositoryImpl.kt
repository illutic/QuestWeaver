package g.sig.questweaver.permissions

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.content.ContextCompat
import g.sig.questweaver.domain.entities.common.Permission
import g.sig.questweaver.domain.repositories.PermissionsRepository

class PermissionsRepositoryImpl(
    private val context: Context,
) : PermissionsRepository {
    override fun hasPermissions(permissions: List<Permission>): Boolean =
        permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it.permission,
            ) == PERMISSION_GRANTED
        }
}
