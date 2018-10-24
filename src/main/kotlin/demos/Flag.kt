package demos

import Text
import org.openrndr.color.ColorRGBa
import org.openrndr.filter.blur.BoxBlur
import org.openrndr.filter.blur.GaussianBlur
import org.openrndr.filter.blur.HashBlur
import org.openrndr.math.Vector2
import org.openrndr.workshop.toolkit.filters.VerticalWaves
import org.openrndr.workshop.toolkit.typography.Fonts
import poster

val Flag: Demo = {
    val txt = listOf(
        "RNDR",
        "X",
        "SPAN"
    )
    val texts = txt.map {
        Text(drawer, it, Fonts.Rubik_Black, 300.0)
    }
    val verticalWaves = VerticalWaves()
    val blur = HashBlur().apply {
        radius = 5.0
        samples = 50
    }
    ({
        poster(drawer) {

            drawer.background(
                ColorRGBa.PINK
            )

            verticalWaves.apply {
                verticalWaves.amplitude = 0.1
                verticalWaves.period = 15.0
            }

            layer(post = listOf(
                verticalWaves.apply {
                    verticalWaves.phase = seconds * 3
                }, blur
            )) {
                drawer.fill = ColorRGBa.PINK.shade(0.2)
                texts.forEachIndexed { i, it ->
                    it.draw(
                        Vector2(
                            width / 2.0,
                            200.0 + i * (it.height + 100.0)
                        ),
                        Text.HorizontalAlign.CENTER,
                        Text.VerticalAlign.ASCENDER
                    )
                }
            }

            layer(post = verticalWaves.apply {
                verticalWaves.phase = seconds * 3 + 15.0
            }) {
                drawer.fill = ColorRGBa.WHITE
                texts.forEachIndexed { i, it ->
                    it.draw(
                        Vector2(
                            width / 2.0,
                            200.0 + i * (it.height + 100.0)
                        ),
                        Text.HorizontalAlign.CENTER,
                        Text.VerticalAlign.ASCENDER
                    )
                }
            }
        }
    })
}