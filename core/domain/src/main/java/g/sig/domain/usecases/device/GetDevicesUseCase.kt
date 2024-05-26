package g.sig.domain.usecases.device

import g.sig.domain.repositories.DeviceRepository

class GetDevicesUseCase(private val repository: DeviceRepository) {

    operator fun invoke() = repository.devices
}