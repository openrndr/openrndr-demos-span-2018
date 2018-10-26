package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector2
import org.openrndr.resourceUrl
import org.openrndr.svg.loadSVG
import java.net.URL

val Vector006: Demo = {

    val svg = loadSVG(URL(resourceUrl("/openrndr.svg")).readText())
    val shapeNodes = svg.findShapes()
    var frames = 0

    {
        drawer.translate(-50.0, 0.0)
        val time = seconds
        val layers = 10
        val density = 20.0
        val points = shapeNodes.flatMap {
            it.shape.contours.flatMap {
                (0..10).flatMap { k ->
                    val base = it.clockwise.sub(0.0, Math.cos(seconds+k*Math.PI/10.0)*0.5+0.5)
                    val offset = base.offset(Math.cos(k * 0.4 + seconds*2.0) * 20.0)
                    offset.equidistantPositions((offset.length / 10.0).toInt())
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

