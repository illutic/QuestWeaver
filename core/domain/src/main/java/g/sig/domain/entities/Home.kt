package g.sig.domain.entities

data class Home(val user: User, val recentGames: List<RecentGame>, val permissions: List<Permission>)
