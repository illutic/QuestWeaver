package g.sig.questweaver.data.serializers

import g.sig.questweaver.data.dto.Dto
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.serializer
import kotlin.reflect.KClass


@OptIn(InternalSerializationApi::class)
internal inline fun <reified T : Dto> serializeDto(dto: T): ByteArray =
    serializeDto(dto, T::class.serializer())

internal inline fun <reified T : Dto> deserializeDto(bytes: ByteArray): T =
    deserializeDto(bytes, T::class) as T

@OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
private fun deserializeDto(bytes: ByteArray, dtoClass: KClass<out Dto>): Dto =
    ProtoBufSerializer.decodeFromByteArray(dtoClass.serializer(), bytes)

@OptIn(ExperimentalSerializationApi::class)
private fun <T : Dto> serializeDto(dto: T, serializer: KSerializer<T>): ByteArray =
    ProtoBufSerializer.encodeToByteArray(serializer, dto)

@OptIn(ExperimentalSerializationApi::class)
private val ProtoBufSerializer = ProtoBuf {
    encodeDefaults = true
}
