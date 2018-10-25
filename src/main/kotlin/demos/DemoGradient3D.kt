package demos

import org.openrndr.math.Vector2
import org.openrndr.shape.Rectangle
import org.openrndr.color.ColorRGBa
import org.openrndr.color.mix
import org.openrndr.workshop.toolkit.typography.Fonts


val DemoGradient3D: Demo = {
    val txt = "KAAMOS"
    val text = Text(drawer, txt, Fonts.SpaceMono_Bold, 1000.0)
    val ls = 150
    val c1 = ColorRGBa.RED
    val c2 = ColorRGBa.BLUE
    val f = 0.2
    val center = Vector2(width / 2.0, height / 2.0)
    val stretchTarget = Rectangle(Vector2(0.0, 0.0), width.toDouble() * 1.0, height.toDouble() * 1.0)
    ({
        drawer.background(
            mix(
                c1, c2,
                Math.abs(Math.cos(seconds * f))
//                  1 -  Math.abs(Math.cos(seconds * f))
            )
        )

//        drawer.translate(center - stretchTarget.center - Vector2(ls / 2.0, 0.0))
        drawer.translate(-20.0, 1.0)
        List(ls) { i ->
            val tv = Vector2(1.0, -1.0) * (ls - i.toDouble())
            text.stretch(stretchTarget.copy(corner = tv)) {
                drawer.background(ColorRGBa.TRANSPARENT)
                val m = Math.abs(Math.cos(seconds * f + i * 0.005)) * ls
                drawer.fill = mix(c1, c2, m / ls.toDouble())
            }
        }
    })
}