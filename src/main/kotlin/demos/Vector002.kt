package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector2
import org.openrndr.resourceUrl
import org.openrndr.svg.loadSVG
import java.net.URL

val Vector002: Demo = {

    val svg = loadSVG(URL(resourceUrl("/openrndr.svg")).readText())
    val shapeNodes = svg.findShapes()
    var frames = 0

    {
        val time = seconds
        val density = Math.cos(seconds)*5.0+10.0
        val points = shapeNodes.flatMap {
            it.shape.contours.flatMap {
                it.equidistantPositions((it.length / density).toInt()).map {
                    it + Vector2(Math.cos(it.y*0.01 + time)*10.0, Math.sin(it.x*0.01 + time)*10.0)
                }
            }
        }
        drawer.background(ColorRGBa.PINK)
        drawer.fill = null
        drawer.stroke = ColorRGBa.BLACK
        drawer.strokeWeight = 2.0
        drawer.circles(points, 10.0)
        frames++
    }
}

