package g.sig.questweaver.domain.entities

data class Home(val user: User, val recentGames: List<Game>, val permissions: List<Permission>)
