package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector2
import org.openrndr.resourceUrl
import org.openrndr.svg.loadSVG
import java.net.URL

val Vector003: Demo = {

    val svg = loadSVG(URL(resourceUrl("/openrndr.svg")).readText())
    val shapeNodes = svg.findShapes()
    var frames = 0

    {
        drawer.translate(-50.0, 0.0)
        val time = seconds
        val layers = 10
        val density = Math.cos(seconds)*10.0 + 15.0
        val points = shapeNodes.flatMap {
            it.shape.contours.flatMap {
                it.equidistantPositions((it.length / density).toInt()).flatMap {

                    (0..layers).map { k ->
                        //it + Vector2(k*10.0 + Math.cos(it.y * 0.01 + time) * 10.0, k*10.0 + Math.sin(it.x * 0.01 + time) * 10.0)
                        it + Vector2(1.0, 1.0) * (10.0 * k)
                    }
                }
            }
        }
        drawer.background(ColorRGBa.PINK)
        drawer.fill = ColorRGBa.PINK
        drawer.stroke = ColorRGBa.BLACK.opacify(0.5)
        drawer.strokeWeight = 2.0
        drawer.circles(points, 10.0)
        frames++
    }
}

