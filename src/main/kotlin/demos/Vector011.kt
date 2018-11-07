package demos

import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector3
import org.openrndr.math.transforms.transform
import org.openrndr.resourceUrl
import org.openrndr.shape.SegmentJoin
import org.openrndr.svg.loadSVG
import java.net.URL

val Vector011: Demo = {
    val svg = loadSVG(URL(resourceUrl("/openrndr.svg")).readText())
    val shapeNodes = svg.findShapes()

    class A(var x: Double, var y: Double, var scale: Double, var length: Double) : Animatable()

    var anims = shapeNodes.mapIndexed { index, it ->
        A(0.0, 0.0, 1.0, 0.0).apply {
            updateAnimation()
            delay(index * 300L)
            animate("dummy", 0.0, 0)
        }
    }

    var frames = 0

    {
        anims.forEachIndexed { index, it ->
            it.updateAnimation()
            if (!it.hasAnimations()) {
                it.animate("scale", 2.0, 500, Easing.QuadInOut)
                it.animate("length", 0.8, 500, Easing.QuadInOut)
                it.complete()
                it.animate("scale", 1.0, 500, Easing.QuadInOut)
                it.animate("length", 0.0, 500, Easing.QuadInOut)
                it.complete()
                it.delay(2000)
                it.animate("dummy", 0.0, 0)
            }
        }
        drawer.translate(-50.0, 0.0)
        var shapeIndex = 0
        val contours = shapeNodes.flatMap {
            val c = it.bounds.center
            shapeIndex++
            it.shape.contours.flatMap {
                (0..3).map { k ->
                    //val base = it.clockwise.sub(0.0, Math.cos(seconds+k*Math.PI/10.0)*0.5+0.5)
                    val offset = it.offset(k * 10.0, SegmentJoin.ROUND).sub(seconds * 0.1 + k * 0.1, seconds * 0.1 + 0.2 + k * 0.1 + anims[shapeIndex - 1].length)
                    offset.transform(transform {
                        translate(0.0, anims[shapeIndex - 1].y)
                        translate(c)
                        rotate(Vector3.UNIT_Z, anims[shapeIndex - 1].length * 15)
                        scale(anims[shapeIndex - 1].scale)
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