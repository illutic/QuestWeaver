package g.sig.domain.usecases.nearby

import g.sig.domain.repositories.NearbyRepository

class CancelAdvertisementUseCase(private val nearbyRepository: NearbyRepository) {
    operator fun invoke() = nearbyRepository.cancelAdvertisement()
}