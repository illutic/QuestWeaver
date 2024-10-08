package g.sig.questweaver.navigation

import androidx.navigation.NamedNavArgument

interface Route {
    val path: String
    val arguments: List<NamedNavArgument> get() = emptyList()
}
