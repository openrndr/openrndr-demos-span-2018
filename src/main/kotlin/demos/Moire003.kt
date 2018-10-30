package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.color.mix
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.renderTarget
import org.openrndr.filter.blur.HashBlur
import org.openrndr.math.Vector2
import org.openrndr.workshop.toolkit.typography.Fonts
import poster
import tools.Mask


val Moire003: Demo = {
    val texts = listOf(
        "OPEN",
        "RNDR"
    ).map {
        Text(drawer, it, Fonts.Rubik_Black, 500.0)
    }

    val state = State(this)

    val blur = HashBlur()

    val colorSet1 = listOf(
        ColorRGBa.BLACK,
        mix(
            ColorRGBa.PINK,
            ColorRGBa.BLACK,
            0.5
        ),
        ColorRGBa.PINK
    )

    val mask = Mask(drawer)

    val rt = renderTarget(width, height) {
        colorBuffer()
    }

    fun drawMoire(colors: List<ColorRGBa>, seconds: Double) {
        drawer.stroke = null
        poster(drawer) {
            drawer.background(ColorRGBa.BLACK)
            layer(post = listOf(
                blur.apply { time = seconds }
            )) {
                drawer.fill = ColorRGBa.BLACK
                state.layers.forEachIndexed { index, layer ->
                    val ps = layer.map { it.position + Vector2(1.0, 1.0) * it.radius }
                    val radius = layer[0].radius * (Math.abs(Math.cos(seconds + index))) + 2.0
                    drawer.fill = colors[index % colors.size]
                    drawer.circles(ps, radius)
                }
            }
        }
    }

    ({

        drawMoire(colorSet1, seconds)

        drawer.isolatedWithTarget(rt) {
            drawMoire(colorSet1, seconds + 2.0)
        }

        val masked = mask.applyTo(rt) {
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
        drawer.image(masked)

    })
}

