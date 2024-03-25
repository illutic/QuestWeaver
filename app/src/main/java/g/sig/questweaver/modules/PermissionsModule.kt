package g.sig.questweaver.modules

import android.os.Build
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import g.sig.domain.entities.Permission
import g.sig.domain.usecases.permissions.GetNearbyPermissionUseCase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PermissionsModule {
    @Provides
    @Singleton
    fun provideAccessWifiStatePermission(): Permission.AccessWifiState =
        Permission.AccessWifiState(android.Manifest.permission.ACCESS_WIFI_STATE)

    @Provides
    @Singleton
    fun provideChangeWifiStatePermission(): Permission.ChangeWifiState =
        Permission.ChangeWifiState(android.Manifest.permission.CHANGE_WIFI_STATE)

    @Provides
    @Singleton
    fun provideBluetoothPermission(): Permission.Bluetooth =
        Permission.Bluetooth(android.Manifest.permission.BLUETOOTH)

    @Provides
    @Singleton
    fun provideBluetoothAdminPermission(): Permission.BluetoothAdmin =
        Permission.BluetoothAdmin(android.Manifest.permission.BLUETOOTH_ADMIN)

    @Provides
    @Singleton
    fun provideAccessCoarseLocationPermission(): Permission.AccessCoarseLocation =
        Permission.AccessCoarseLocation(android.Manifest.permission.ACCESS_COARSE_LOCATION)

    @Provides
    @Singleton
    fun provideAccessFineLocationPermission(): Permission.AccessFineLocation =
        Permission.AccessFineLocation(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Provides
    @Singleton
    fun provideBluetoothAdvertisePermission(): Permission.BluetoothAdvertise =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Permission.BluetoothAdvertise(android.Manifest.permission.BLUETOOTH_ADVERTISE)
        } else {
            Permission.BluetoothAdvertise("")
        }

    @Provides
    @Singleton
    fun provideBluetoothConnectPermission(): Permission.BluetoothConnect =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Permission.BluetoothConnect(android.Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            Permission.BluetoothConnect("")
        }

    @Provides
    @Singleton
    fun provideBluetoothScanPermission(): Permission.BluetoothScan =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Permission.BluetoothScan(android.Manifest.permission.BLUETOOTH_SCAN)
        } else {
            Permission.BluetoothScan("")
        }

    @Provides
    @Singleton
    fun provideNearbyWifiDevicesPermission(): Permission.NearbyWifiDevices =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Permission.NearbyWifiDevices(android.Manifest.permission.NEARBY_WIFI_DEVICES)
        } else {
            Permission.NearbyWifiDevices("")
        }

    @Provides
    @Singleton
    fun provideReadExternalStoragePermission(): Permission.ReadExternalStorage =
        Permission.ReadExternalStorage(android.Manifest.permission.READ_EXTERNAL_STORAGE)

    @Provides
    @Singleton
    fun provideReadPhotosPermission(): Permission.ReadPhotos =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Permission.ReadPhotos(android.Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            Permission.ReadPhotos("")
        }

    @Provides
    @Singleton
    fun provideReadVideosPermission(): Permission.ReadVideos =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Permission.ReadVideos(android.Manifest.permission.READ_MEDIA_VIDEO)
        } else {
            Permission.ReadVideos("")
        }

    @Provides
    @Singleton
    fun provideReadAudioPermission(): Permission.ReadAudio =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Permission.ReadAudio(android.Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            Permission.ReadAudio("")
        }

    private fun getNearbyPermissionsBasedOnSdkVersion(
        accessWifiState: Permission.AccessWifiState,
        changeWifiState: Permission.ChangeWifiState,
        bluetooth: Permission.Bluetooth,
        bluetoothAdmin: Permission.BluetoothAdmin,
        accessCoarseLocation: Permission.AccessCoarseLocation,
        accessFineLocation: Permission.AccessFineLocation,
        bluetoothAdvertise: Permission.BluetoothAdvertise,
        bluetoothConnect: Permission.BluetoothConnect,
        bluetoothScan: Permission.BluetoothScan,
        nearbyWifiDevices: Permission.NearbyWifiDevices,
        readExternalStorage: Permission.ReadExternalStorage,
        readPhotos: Permission.ReadPhotos,
        readVideos: Permission.ReadVideos,
        readAudio: Permission.ReadAudio,
    ): List<Permission> {
        val version = Build.VERSION.SDK_INT
        val permissions = mutableListOf(
            accessWifiState,
            changeWifiState,
            bluetooth,
            bluetoothAdmin,
            accessCoarseLocation,
            accessFineLocation,
            bluetoothAdvertise,
            bluetoothConnect,
            bluetoothScan,
            nearbyWifiDevices,
            readExternalStorage,
            readPhotos,
            readVideos,
            readAudio,
        )
        if (version >= Build.VERSION_CODES.TIRAMISU) {
            permissions.remove(readExternalStorage)
        } else {
            permissions.remove(readPhotos)
            permissions.remove(readVideos)
            permissions.remove(readAudio)
        }

        if (version < Build.VERSION_CODES.S_V2) {
            permissions.remove(nearbyWifiDevices)
        }

        if (version > Build.VERSION_CODES.S) {
            permissions.remove(accessWifiState)
            permissions.remove(changeWifiState)
            permissions.remove(accessFineLocation)
        } else {
            permissions.remove(bluetoothAdvertise)
            permissions.remove(bluetoothConnect)
            permissions.remove(bluetoothScan)
        }

        if (version > Build.VERSION_CODES.R) {
            permissions.remove(bluetooth)
            permissions.remove(bluetoothAdmin)
        }

        if (version > Build.VERSION_CODES.P) {
            permissions.remove(accessCoarseLocation)
        }

        if (version < Build.VERSION_CODES.Q) {
            permissions.remove(accessFineLocation)
        }

        return permissions
    }

    @Provides
    @Singleton
    fun provideGetNearbyPermissionUseCase(
        accessWifiState: Permission.AccessWifiState,
        changeWifiState: Permission.ChangeWifiState,
        bluetooth: Permission.Bluetooth,
        bluetoothAdmin: Permission.BluetoothAdmin,
        accessCoarseLocation: Permission.AccessCoarseLocation,
        accessFineLocation: Permission.AccessFineLocation,
        bluetoothAdvertise: Permission.BluetoothAdvertise,
        bluetoothConnect: Permission.BluetoothConnect,
        bluetoothScan: Permission.BluetoothScan,
        nearbyWifiDevices: Permission.NearbyWifiDevices,
        readExternalStorage: Permission.ReadExternalStorage,
        readPhotos: Permission.ReadPhotos,
        readVideos: Permission.ReadVideos,
        readAudio: Permission.ReadAudio,
    ): GetNearbyPermissionUseCase {
        val permissions = getNearbyPermissionsBasedOnSdkVersion(
            accessWifiState,
            changeWifiState,
            bluetooth,
            bluetoothAdmin,
            accessCoarseLocation,
            accessFineLocation,
            bluetoothAdvertise,
            bluetoothConnect,
            bluetoothScan,
            nearbyWifiDevices,
            readExternalStorage,
            readPhotos,
            readVideos,
            readAudio,
        ).toTypedArray()

        return GetNearbyPermissionUseCase(*permissions)
    }
}