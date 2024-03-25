package g.sig.permissions

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import g.sig.domain.usecases.permissions.GetNearbyPermissionUseCase
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    nearbyPermissions: GetNearbyPermissionUseCase
) : ViewModel() {
    val permissions = nearbyPermissions().map { it.permission }
}