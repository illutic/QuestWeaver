package g.sig.questweaver.data.datasources.user

import androidx.datastore.core.Serializer
import g.sig.questweaver.data.dto.UserDto
import g.sig.questweaver.data.serializers.deserializeDto
import g.sig.questweaver.data.serializers.serializeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

object UserSerializer : Serializer<UserDto> {
    override val defaultValue: UserDto = UserDto.Empty

    override suspend fun readFrom(input: InputStream): UserDto =
        withContext(Dispatchers.IO) {
            deserializeDto(input.readBytes())
        }

    override suspend fun writeTo(
        t: UserDto,
        output: OutputStream,
    ) = withContext(Dispatchers.IO) { output.write(serializeDto(t)) }
}
