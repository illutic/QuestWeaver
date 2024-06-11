package g.sig.questweaver.data.datasources.user

import androidx.datastore.core.Serializer
import g.sig.questweaver.data.entities.common.UserDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream

object UserSerializer : Serializer<UserDto> {
    override val defaultValue: UserDto = UserDto.Empty

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(input: InputStream): UserDto =
        ProtoBuf.decodeFromByteArray(input.readBytes())

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeTo(t: UserDto, output: OutputStream) =
        withContext(Dispatchers.IO) {
            output.write(ProtoBuf.encodeToByteArray(t))
        }
}
