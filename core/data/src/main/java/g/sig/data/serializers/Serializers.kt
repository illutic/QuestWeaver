package g.sig.data.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
val ProtoBufSerializer = ProtoBuf {
    encodeDefaults = true
}