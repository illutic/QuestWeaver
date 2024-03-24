package g.sig.data.serializers.proto

import androidx.datastore.core.Serializer
import g.sig.data.entities.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream

object UserSerializer : Serializer<User> {
    override val defaultValue: User = User.Empty

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): User =
        ProtoBuf.decodeFromByteArray(input.readBytes())

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeTo(t: User, output: OutputStream) =
        withContext(Dispatchers.IO) {
            output.write(ProtoBuf.encodeToByteArray(t))
        }
}