import demos.Demo
import demos.Text
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolated
import org.openrndr.math.Vector2
import org.openrndr.shape.Rectangle
import org.openrndr.workshop.toolkit.typography.Fonts

val TextDebug: Demo = {
    val word = "SPAN 2018"
    val text = Text(drawer, word, Fonts.SpaceMono_Bold, 2000.0);

    val boundsToFill = Rectangle(
        0.0,
        0.0,
        width * 0.5,
        height * 0.5
    )


    ({
        drawer.stroke = null
        drawer.background(ColorRGBa.WHITE)
        drawer.fill = ColorRGBa.BLACK

//        text.explain(
//            Vector2(width / 2.0, height / 2.0),
//            demos.Text.HorizontalAlign.CENTER,
//            demos.Text.VerticalAlign.CENTER
//        )


        text.stretch(boundsToFill) {
            drawer.background(ColorRGBa.PINK)
            drawer.fill = ColorRGBa.WHITE
        }

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
    })
}

