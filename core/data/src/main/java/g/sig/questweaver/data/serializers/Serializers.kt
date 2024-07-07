@file:OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)

package g.sig.questweaver.data.serializers

import g.sig.questweaver.data.dto.Dto
import g.sig.questweaver.data.dto.PayloadDataDto
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

internal fun serializePayloadData(dto: PayloadDataDto): ByteArray =
    ProtoBufSerializer
        .encodeToByteArray<PayloadDataDto>(dto)

internal fun deserializePayloadData(bytes: ByteArray): PayloadDataDto =
    ProtoBufSerializer
        .decodeFromByteArray<PayloadDataDto>(bytes)

@OptIn(InternalSerializationApi::class)
internal inline fun <reified T : Dto> serializeDto(dto: T): ByteArray =
    serializeDto(
        dto,
        T::class.serializer(),
    )

internal inline fun <reified T : Dto> deserializeDto(bytes: ByteArray): T =
    deserializeDto(
        bytes,
        T::class,
    ) as T

private fun deserializeDto(
    bytes: ByteArray,
    dtoClass: KClass<out Dto>,
): Dto = ProtoBufSerializer.decodeFromByteArray(dtoClass.serializer(), bytes)

private fun <T : Dto> serializeDto(
    dto: T,
    serializer: KSerializer<T>,
): ByteArray = ProtoBufSerializer.encodeToByteArray(serializer, dto)

private val ProtoBufSerializer =
    ProtoBuf {
        encodeDefaults = true
    }
