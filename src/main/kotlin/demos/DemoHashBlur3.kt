package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.color.mix
import org.openrndr.filter.blend.add
import org.openrndr.filter.blur.HashBlur
import org.openrndr.math.Vector2
import org.openrndr.workshop.toolkit.typography.Fonts
import poster


val DemoHashBlur3: Demo = {
    val str = "SAUNA"
    val size = 500.0
    val text1 = Text(drawer, str, Fonts.Rubik_Black, 600.0)
    val text2 = Text(drawer, str, Fonts.Rubik_Black, size)

    val bblur = HashBlur()
    val fblur = org.openrndr.filter.blur.HashBlur()
    fun() {
        drawer.stroke = null
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

            layer(post = fblur, blend = add) {
                drawer.fill = mix(
                    ColorRGBa.RED,
                    ColorRGBa.YELLOW,
                    0.5
                )
                text2.draw(
                    Vector2(width / 2.0, height / 2.0),
                    Text.HorizontalAlign.CENTER,
                    Text.VerticalAlign.CENTER
                )
            }


            layer(post = fblur.apply {
                radius = 3.0
            }, blend = add) {
                drawer.fill = mix(
                    ColorRGBa.RED,
                    ColorRGBa.YELLOW,
                    0.5
                ).opacify(0.7)
                text2.draw(
                    Vector2(width / 2.0, height / 2.0),
                    Text.HorizontalAlign.CENTER,
                    Text.VerticalAlign.CENTER
                )
            }
        }
    }
}

