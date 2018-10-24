package demos

import org.openrndr.color.ColorRGBa
import Text
import org.openrndr.color.mix
import org.openrndr.filter.blend.add
import org.openrndr.filter.blur.HashBlur
import org.openrndr.math.Vector2
import org.openrndr.math.map
import org.openrndr.workshop.toolkit.typography.Fonts
import poster


val DemoHashBlur4: Demo = {
    val str = "SUN"
    val size = 500.0
    val text1 = Text(drawer, str, Fonts.Rubik_Black, 600.0)
    val text2 = Text(drawer, str, Fonts.Rubik_Black, size)

    val bblur = HashBlur()
    val fblur = org.openrndr.filter.blur.HashBlur()
    fun() {
        drawer.stroke = null
        val r = map(0.0, width.toDouble(), 0.0, 1.0, mouse.position.x)
        poster(drawer) {
            bblur.apply {
                radius = 30.0
                time = seconds
                samples = 15
                gain = 2.0
            }

            layer(post = bblur) {
                drawer.fill = mix(ColorRGBa.BLUE, ColorRGBa.RED, 0.7)
                text1.draw(
                    Vector2(width / 2.0, height / 2.0),
                    Text.HorizontalAlign.CENTER,
                    Text.VerticalAlign.CENTER
                )
            }

            fblur.apply {
                radius = 15.0
                time = seconds
                samples = 50
                gain = 1.0
            }

            val n = 5
            (1..n).forEach { i ->
                val m = i.toDouble() / n
                layer(post = fblur.apply {
                    radius = 1.0 + m * 10.0
                }, blend = add) {
                    drawer.fill = mix(
                        ColorRGBa.RED,
                        ColorRGBa.YELLOW,
                        m
                    ).opacify(0.3)
                    text2.draw(
                        Vector2(width / 2.0 - i * 10.0, height / 2.0 + i * 5.0),
                        Text.HorizontalAlign.CENTER,
                        Text.VerticalAlign.CENTER
                    )
                }
            }
        }
    }
}

