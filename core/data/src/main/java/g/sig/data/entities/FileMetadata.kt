package g.sig.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class FileMetadata(val name: String, val extension: String)
