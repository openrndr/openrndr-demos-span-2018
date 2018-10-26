package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector3
import org.openrndr.math.transforms.transform
import org.openrndr.resourceUrl
import org.openrndr.shape.SegmentJoin
import org.openrndr.svg.loadSVG
import java.net.URL
val Vector009: Demo = {
    val svg = loadSVG(URL(resourceUrl("/openrndr.svg")).readText())
    val shapeNodes = svg.findShapes()
    var frames = 0
    {
        drawer.translate(-50.0, 0.0)
        var shapeIndex = 0
        val contours = shapeNodes.flatMap {
            val c = it.bounds.center
            shapeIndex++
            it.shape.contours.flatMap {
                (0..3).map { k ->
                    //val base = it.clockwise.sub(0.0, Math.cos(seconds+k*Math.PI/10.0)*0.5+0.5)
                    val offset = it.offset(k * 10.0, SegmentJoin.ROUND).sub(Math.sin(shapeIndex+seconds+k*Math.PI/10.0)*0.5+0.5, Math.cos(shapeIndex+seconds+k*Math.PI/10.0)*0.5+0.5)
                    offset.transform(transform {
                        translate(0.0, Math.sin(shapeIndex+seconds)*140.0)

                        translate(c)
                        rotate(Vector3.UNIT_Z, Math.sin(shapeIndex+seconds)*15.0)
                        translate(-c)
                    })
                }
            }
        }
        drawer.background(ColorRGBa.BLACK)
        drawer.fill = null
        drawer.stroke = ColorRGBa.PINK
        drawer.strokeWeight = 2.0
        drawer.contours(contours.filter { it.segments.isNotEmpty() })
        frames++
    }
}