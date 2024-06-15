package g.sig.questweaver.data

import g.sig.questweaver.data.dto.ColorDto
import g.sig.questweaver.data.dto.Dto
import g.sig.questweaver.data.dto.PointDto
import g.sig.questweaver.data.serializers.deserializeDto
import g.sig.questweaver.data.serializers.serializeDto
import org.junit.Assert.assertEquals
import org.junit.Test

class SerializerTest {
    @Test
    fun testColor() {
        val color = ColorDto(0x123456u)
        val bytes = serializeDto(color)
        val decoded = deserializeDto<Dto>(bytes)
        assertEquals(color, decoded)
    }

    @Test
    fun testPoint() {
        val point = PointDto(1.0f, 2.0f)
        val bytes = serializeDto(point)
        val decoded = deserializeDto<Dto>(bytes)
        assertEquals(point, decoded)
    }

    @Test
    fun testSize() {
        val size = PointDto(1.0f, 2.0f)
        val bytes = serializeDto(size)
        val decoded = deserializeDto<Dto>(bytes)
        assertEquals(size, decoded)
    }
}
