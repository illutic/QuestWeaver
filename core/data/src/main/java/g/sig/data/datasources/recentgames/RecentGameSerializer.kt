package g.sig.data.datasources.recentgames

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream

internal object RecentGameSerializer : Serializer<Games> {
    override val defaultValue: Games = Games(emptyList())

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): Games =
        ProtoBuf.decodeFromByteArray(input.readBytes())

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeTo(t: Games, output: OutputStream) =
        withContext(Dispatchers.IO) {
            output.write(ProtoBuf.encodeToByteArray(t))
        }
}