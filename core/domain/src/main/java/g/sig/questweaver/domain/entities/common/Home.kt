package g.sig.questweaver.domain.entities.common

import g.sig.questweaver.domain.entities.DomainEntity

data class Home(
    val user: User,
    val recentGames: List<Game>,
    val permissions: List<Permission>
) : DomainEntity
