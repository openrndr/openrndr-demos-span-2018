package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.resourceUrl
import org.openrndr.svg.loadSVG
import java.net.URL

val Vector001: Demo = {

    val svg = loadSVG(URL(resourceUrl("/openrndr.svg")).readText())
    val shapeNodes = svg.findShapes()
    var frames = 0

    {
        val density = Math.cos(seconds)*5.0+10.0
        val points = shapeNodes.flatMap {
            it.shape.contours.flatMap {
                it.equidistantPositions((it.length / density).toInt())
            }
        }
        drawer.background(ColorRGBa.PINK)
        drawer.fill = null
        drawer.stroke = ColorRGBa.BLACK
        drawer.strokeWeight = 2.0
        drawer.circles(points.subList(((Math.cos(seconds + Math.PI) * 0.25 + 0.25) * points.size).toInt(), ((Math.cos(seconds) * 0.25 + 0.75) * points.size).toInt()), 10.0)
        frames++
    }
}

