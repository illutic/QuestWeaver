package g.sig.questweaver.domain.entities

sealed interface PayloadData {
    val data: DomainEntity

    data class Broadcast(
        override val data: DomainEntity
    ) : PayloadData

    data class Unicast(
        override val data: DomainEntity,
        val destination: String
    ) : PayloadData
}

data class IncomingPayload(
    val origin: String,
    val payloadData: PayloadData
)
