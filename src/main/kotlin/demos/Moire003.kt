package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.filter.blur.HashBlur
import org.openrndr.math.Vector2
import org.openrndr.shape.Rectangle
import org.openrndr.workshop.toolkit.typography.Fonts
import poster
import java.awt.SystemColor.text


val Moire003: Demo = {
    val texts = listOf(
            "OPEN",
            "RNDR"
    ).map {
        Text(drawer, it, Fonts.Rubik_Black, 500.0)
    }

    val nLayers = 3

    fun mkCircles(radius: Double, step: Int): List<C> {
        return (0..width step step).flatMap { x ->
            (0..height step step).map { y ->
                C(Vector2(x.toDouble(), y.toDouble()), radius)
            }
        }
    }

    fun getLayers(): List<List<C>> {
        val bgcircles = mkCircles(10.0, 50)
        return listOf(
                bgcircles
        ) + (0..nLayers).map {
            val r = 5.0 + Math.random() * 50.0
            val g = (r * 2.0 + 1.0 * Math.random() * 50.0).toInt()
            val circles = mkCircles(r, g)
            circles
        }
    }

    var ls = getLayers()

    mouse.clicked.listen {
        ls = getLayers()
    }

    val blur = HashBlur()
    val colors = listOf(
//        ColorRGBa.BLACK,
//        ColorRGBa.WHITE
            ColorRGBa.RED,
            ColorRGBa.GREEN,
            ColorRGBa.BLUE
    )

    val mask = Mask(width, height)

    ({
        drawer.stroke = null

        poster(drawer) {
            drawer.background(ColorRGBa.BLACK)
            layer(post = listOf(
                    blur.apply { time = seconds }
            )) {
                drawer.fill = ColorRGBa.BLACK
                ls.forEachIndexed { index, layer ->
                    val ps = layer.map { it.position }
                    val radius = layer[0].radius * (Math.abs(Math.cos(seconds + index))) + 2.0
                    drawer.fill = colors[index % colors.size]
                    drawer.circles(ps, radius)
                }
            }
        }
        mask.apply(drawer) {
            texts.forEachIndexed { index, text ->
                text.draw(Vector2(width / 2.0 - (if (index == 0) 10.0 else 0.0),
                        height / 2.0 - if (index == 0) {
                            (text.height / 2.0) + 10.0
                        } else {
                            -(text.height / 2.0 + 10.0)
                        }
                ),
                        Text.HorizontalAlign.CENTER,
                        Text.VerticalAlign.CENTER
                )
            }
        }
    })
}

