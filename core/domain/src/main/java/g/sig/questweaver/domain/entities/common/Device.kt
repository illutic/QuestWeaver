package g.sig.questweaver.domain.entities.common

import g.sig.questweaver.domain.entities.DomainEntity
import g.sig.questweaver.domain.entities.states.ConnectionState

data class Device(
    val id: String,
    val name: String,
    val connectionState: ConnectionState,
) : DomainEntity
