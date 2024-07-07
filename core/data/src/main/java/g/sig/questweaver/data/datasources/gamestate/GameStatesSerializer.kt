package g.sig.questweaver.data.datasources.gamestate

import androidx.datastore.core.Serializer
import g.sig.questweaver.data.dto.GameStatesDto
import g.sig.questweaver.data.serializers.deserializeDto
import g.sig.questweaver.data.serializers.serializeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

internal object GameStatesSerializer : Serializer<GameStatesDto> {
    override val defaultValue: GameStatesDto = GameStatesDto(emptyList())

    override suspend fun readFrom(input: InputStream): GameStatesDto =
        withContext(Dispatchers.IO) {
            deserializeDto(input.readBytes())
        }

    override suspend fun writeTo(
        t: GameStatesDto,
        output: OutputStream,
    ) = withContext(Dispatchers.IO) { output.write(serializeDto(t)) }
}
