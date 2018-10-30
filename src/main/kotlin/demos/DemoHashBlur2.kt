package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.color.mix
import org.openrndr.filter.blend.subtract
import org.openrndr.filter.blur.HashBlur
import org.openrndr.math.Vector2
import org.openrndr.workshop.toolkit.typography.Fonts
import poster


val DemoHashBlur2: Demo = {
    val text = Text(drawer, "SPAN", Fonts.Rubik_Black, 700.0)

    val bblur = HashBlur()
    val fblur = HashBlur()

    fun() {
        drawer.stroke = null
        poster(drawer) {
            bblur.apply {
                radius = 30.0
                time = seconds
                samples = 60
                gain = 2.0
            }
            layer(post = bblur) {
                drawer.fill = mix(ColorRGBa.PINK, ColorRGBa.RED, 0.3)
                List(4) { i ->
                    drawer.circle(Vector2(
                        (width / 4.0) * (1 + i),
                        height / 2.0
                    ), 300.0)
                }
            }
            fblur.apply {
                radius = 11.0
                time = seconds
                samples = 50
                gain = 1.0
            }
            layer(post = fblur, blend = subtract) {
                drawer.fill = ColorRGBa.BLACK
                text.draw(
                    Vector2(width / 2.0, height / 2.0),
                    Text.HorizontalAlign.CENTER,
                    Text.VerticalAlign.CENTER
                )
            }
        }
    }
}

