package g.sig.questweaver.domain.usecases.device

import g.sig.questweaver.domain.repositories.DeviceRepository

class GetDevicesUseCase(private val repository: DeviceRepository) {

    operator fun invoke() = repository.devices
}
