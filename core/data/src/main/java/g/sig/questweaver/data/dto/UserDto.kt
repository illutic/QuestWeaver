package g.sig.questweaver.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(val id: String, val name: String) : Dto {
    companion object {
        val Empty = UserDto("", "")
    }
}
