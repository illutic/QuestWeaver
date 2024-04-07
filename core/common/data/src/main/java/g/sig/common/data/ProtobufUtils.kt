package g.sig.common.data

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> ByteArray.toObject() = ProtoBuf.decodeFromByteArray<T>(this)

@OptIn(ExperimentalSerializationApi::class)
fun <T> ByteArray.toObjectOrNull(clazz: DeserializationStrategy<@Serializable T>) = try {
    ProtoBuf.decodeFromByteArray(clazz, this)
} catch (e: IllegalArgumentException) {
    e.printStackTrace()
    null
}
