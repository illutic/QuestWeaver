package g.sig.questweaver.data.datasources.recentgames

import androidx.datastore.core.Serializer
import g.sig.questweaver.data.dto.GamesDto
import g.sig.questweaver.data.serializers.deserializeDto
import g.sig.questweaver.data.serializers.serializeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

internal object RecentGameSerializer : Serializer<GamesDto> {
    override val defaultValue: GamesDto = GamesDto(emptyList())

    override suspend fun readFrom(input: InputStream): GamesDto =
        withContext(Dispatchers.IO) {
            deserializeDto(input.readBytes())
        }

    override suspend fun writeTo(
        t: GamesDto,
        output: OutputStream,
    ) = withContext(Dispatchers.IO) { output.write(serializeDto(t)) }
}
