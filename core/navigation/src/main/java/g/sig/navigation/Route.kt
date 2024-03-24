package g.sig.navigation

interface Route {
    val path: String
    val arguments: List<NavArg> get() = emptyList()
}