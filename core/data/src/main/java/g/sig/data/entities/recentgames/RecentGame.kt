package g.sig.data.entities.recentgames

import kotlinx.serialization.Serializable
import g.sig.domain.entities.RecentGame as RecentGameDomain

@Serializable
data class RecentGame(val id: String, val title: String, val imageUri: String) {
    fun toDomain() = RecentGameDomain(id, title, imageUri)

    companion object {
        val Empty = RecentGame("", "", "")

        fun RecentGame.fromDomain() = RecentGame(id, title, imageUri)
    }
}
