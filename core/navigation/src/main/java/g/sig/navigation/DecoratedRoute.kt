package g.sig.navigation

interface DecoratedRoute {
    val route: Route
    val label: String
    val unselectedIcon: NavIcon
    val selectedIcon: NavIcon
}