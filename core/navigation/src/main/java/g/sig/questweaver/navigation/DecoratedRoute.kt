package g.sig.questweaver.navigation

interface DecoratedRoute : Route {
    val label: String
    val unselectedIcon: NavIcon
    val selectedIcon: NavIcon
}
