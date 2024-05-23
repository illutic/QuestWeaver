package g.sig.navigation

interface DecoratedRoute : Route {
    val label: String
    val unselectedIcon: NavIcon
    val selectedIcon: NavIcon
}