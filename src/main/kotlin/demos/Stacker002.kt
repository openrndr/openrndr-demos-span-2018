package demos

import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.color.mix
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.math.Vector2
import org.openrndr.math.mix
import org.openrndr.workshop.toolkit.typography.Fonts


val Stacker002: Demo = {
    val txt = "OPENRNDR"

    val n = 50
    val texts = txt.map { it ->
        Text(drawer, it.toString(), Fonts.SpaceMono_Bold, 700.0)
    }.mapIndexed { index, text ->
        Stacker(n, 5.0) { i ->
            drawer.fill = mix(
                ColorRGBa.WHITE,
                ColorRGBa.BLACK,
                (i / n.toDouble())
            )
            text.draw(
                Vector2(100.0 + index * text.width * 0.8, 0.0),
                Text.HorizontalAlign.CENTER,
                Text.VerticalAlign.CENTER
            )
        }
    }

    ({
        drawer.background(ColorRGBa.BLACK)
        drawer.translate(50.0, 0.0)
        List(8) { yi ->
            drawer.translate(0.0, yi * 40.0)
            texts.forEachIndexed { index, it ->
                it.draw(drawer, seconds + index)
            }
        }

    })
}