package g.sig.domain.usecases.nearby

import g.sig.domain.repositories.NearbyRepository

class CancelDiscoveryUseCase(private val nearbyRepository: NearbyRepository) {
    operator fun invoke() = nearbyRepository.cancelDiscovery()
}