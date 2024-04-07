package g.sig.data.entities

import g.sig.common.data.toObject
import g.sig.common.data.toObjectOrNull
import g.sig.data.nearby.entities.Data
import kotlinx.serialization.DeserializationStrategy

fun Data.asDataEntity(): DataEntity = when (this) {
    is Data.File -> DataEntity.File(uri, fileMetadata.byteArray.toObject())
    is Data.Message -> DataEntity.Message(byteArray)
    is Data.Stream -> DataEntity.Stream(inputStream)
}

fun DataEntity.asMessage() = this as? DataEntity.Message

fun <T> DataEntity.getDataOrNull(deserializationStrategy: DeserializationStrategy<T>) = asMessage()?.content?.toObjectOrNull(deserializationStrategy)