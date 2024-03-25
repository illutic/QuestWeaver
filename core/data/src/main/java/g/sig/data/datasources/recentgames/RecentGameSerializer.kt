package g.sig.data.datasources.recentgames

import androidx.datastore.core.Serializer
import g.sig.data.entities.recentgames.RecentGame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream

object RecentGameSerializer : Serializer<RecentGame> {
    override val defaultValue: RecentGame = RecentGame.Empty

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): RecentGame =
        ProtoBuf.decodeFromByteArray(input.readBytes())

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeTo(t: RecentGame, output: OutputStream) =
        withContext(Dispatchers.IO) {
            output.write(ProtoBuf.encodeToByteArray(t))
        }
}