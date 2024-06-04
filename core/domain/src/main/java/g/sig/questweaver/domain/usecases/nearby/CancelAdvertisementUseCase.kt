package g.sig.questweaver.domain.usecases.nearby

import g.sig.questweaver.domain.repositories.NearbyRepository

class CancelAdvertisementUseCase(private val nearbyRepository: NearbyRepository) {
    operator fun invoke() = nearbyRepository.cancelAdvertisement()
}
