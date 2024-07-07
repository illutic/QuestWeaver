package g.sig.questweaver.domain.entities.common

import g.sig.questweaver.domain.entities.DomainEntity

sealed interface Permission : DomainEntity {
    val permission: String

    data class AccessWifiState(
        override val permission: String,
    ) : Permission

    data class ChangeWifiState(
        override val permission: String,
    ) : Permission

    data class Bluetooth(
        override val permission: String,
    ) : Permission

    data class BluetoothAdmin(
        override val permission: String,
    ) : Permission

    data class AccessCoarseLocation(
        override val permission: String,
    ) : Permission

    data class AccessFineLocation(
        override val permission: String,
    ) : Permission

    data class BluetoothAdvertise(
        override val permission: String,
    ) : Permission

    data class BluetoothConnect(
        override val permission: String,
    ) : Permission

    data class BluetoothScan(
        override val permission: String,
    ) : Permission

    data class NearbyWifiDevices(
        override val permission: String,
    ) : Permission

    data class ReadExternalStorage(
        override val permission: String,
    ) : Permission

    data class ReadPhotos(
        override val permission: String,
    ) : Permission

    data class ReadVideos(
        override val permission: String,
    ) : Permission

    data class ReadAudio(
        override val permission: String,
    ) : Permission
}
