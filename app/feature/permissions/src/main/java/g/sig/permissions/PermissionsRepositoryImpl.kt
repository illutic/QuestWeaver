package g.sig.permissions

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.content.ContextCompat
import g.sig.domain.entities.Permission
import g.sig.domain.repositories.PermissionsRepository

class PermissionsRepositoryImpl(private val context: Context) : PermissionsRepository {
    override fun hasPermissions(vararg permission: Permission): Boolean =
        permission.all { ContextCompat.checkSelfPermission(context, it.permission) == PERMISSION_GRANTED }
}