package g.sig.questweaver.domain.entities.common

import g.sig.questweaver.domain.entities.DomainEntity

data class User(
    val name: String,
    val id: String,
) : DomainEntity {
    companion object {
        val Empty = User("", "")
    }
}
