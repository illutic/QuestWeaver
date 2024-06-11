package g.sig.questweaver.domain.entities

import g.sig.questweaver.domain.entities.blocks.Point
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test


class InteractionsTest {
    private val normalizedPoints = listOf(
        Point(0f, 0f),
        Point(0.05f, 0.05f),
        Point(0.1f, 0.1f),
        Point(0.15f, 0.15f),
        Point(0.2f, 0.2f)
    )

    @Test
    fun `drawing only streams when the distance is greater than the threshold`() = runTest {
        val pointsFlow = normalizedPoints.asFlow()
        val drawing = Interaction.Drawing(pointsFlow)

        val points = drawing.pointsToStream.toList()

        assertEquals(
            listOf(
                Point(0f, 0f),
                Point(0.1f, 0.1f),
                Point(0.2f, 0.2f)
            ),
            points
        )
    }
}
