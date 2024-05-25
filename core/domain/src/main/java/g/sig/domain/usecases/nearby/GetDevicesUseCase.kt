package g.sig.domain.usecases.nearby

import g.sig.domain.repositories.DeviceRepository

class GetDevicesUseCase(private val repository: DeviceRepository) {

    operator fun invoke() = repository.devices
}