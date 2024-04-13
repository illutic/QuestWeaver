package g.sig.data.entities

import g.sig.common.data.toObject
import g.sig.common.data.toObjectOrNull
import g.sig.data.nearby.entities.Data
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

fun Data.asDataEntity(): DataEntity = when (this) {
    is Data.File -> DataEntity.File(uri, fileMetadata.byteArray.toObject())
    is Data.Message -> DataEntity.Message(byteArray)
    is Data.Stream -> DataEntity.Stream(inputStream)
}

@OptIn(ExperimentalSerializationApi::class)
fun DataEntity.asData(): Data = when (this) {
    is DataEntity.File -> Data.File(uri, Data.Message(ProtoBuf.encodeToByteArray<FileMetadata>(metadata)))
    is DataEntity.Message -> Data.Message(content)
    is DataEntity.Stream -> Data.Stream(inputStream)
}

fun DataEntity.asMessage() = this as? DataEntity.Message

fun <T> DataEntity.getDataOrNull(deserializationStrategy: DeserializationStrategy<T>) = asMessage()?.content?.toObjectOrNull(deserializationStrategy)