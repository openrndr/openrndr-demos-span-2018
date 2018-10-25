package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.color.mix
import org.openrndr.math.Vector2
import org.openrndr.workshop.toolkit.filters.ZoomMosaic
import org.openrndr.workshop.toolkit.typography.Fonts
import poster
import kotlin.math.roundToInt

val Mozaic: Demo = {
    val txt = "OPENRNDR"
    val text = Text(drawer, txt, Fonts.SpaceMono_Bold, 300.0)
    val ls = height / text.height
    val zoomMosaic = ZoomMosaic()
    ({
        drawer.background(ColorRGBa.BLACK)
        zoomMosaic.apply {
            scale = Math.cos(seconds) + 2.0
            xSteps = 50
            ySteps = 50
        }
        poster(drawer) {
            layer(post = zoomMosaic) {
                List(ls.roundToInt()) { i ->
                    drawer.fill = mix(
                        ColorRGBa.PINK,
                        ColorRGBa.BLACK,
                        Math.abs(Math.cos(seconds + i))
                    )
                    text.draw(
                        Vector2(
                            width / 2.0 + Math.cos(seconds + i * 5) * (width / 2.0 - text.width / 2.0),
                            i.toDouble() * (height / ls)
                        ),
                        Text.HorizontalAlign.CENTER,
                        Text.VerticalAlign.ASCENDER
                    )
                }
            }
        }
    })
}