import demos.Demo
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolated
import org.openrndr.math.Vector2
import org.openrndr.workshop.toolkit.typography.Fonts

val TextDebug: Demo = {
    val word = "SPAN"
    val text = Text(drawer, word, Fonts.Hanken_Book, 100.0);
    {
        drawer.stroke = null
        drawer.background(ColorRGBa.WHITE)
        drawer.fill = ColorRGBa.BLACK

        text.explain(
            Vector2(width / 2.0, height / 2.0),
            Text.HorizontalAlign.CENTER,
            Text.VerticalAlign.CENTER_ASCENDER
        )

        drawer.isolated {
            drawer.stroke = ColorRGBa.GREEN
            drawer.lineSegment(
                Vector2(width / 2.0, 0.0),
                Vector2(width / 2.0, height.toDouble())
            )
            drawer.lineSegment(
                Vector2(0.0, height / 2.0),
                Vector2(width.toDouble(), height / 2.0)
            )
        }
    }
}

