package demos

import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector3
import org.openrndr.math.transforms.transform
import org.openrndr.resourceUrl
import org.openrndr.shape.SegmentJoin
import org.openrndr.shape.Shape
import org.openrndr.svg.loadSVG
import java.net.URL

val Vector012: Demo = {
    val svg = loadSVG(URL(resourceUrl("/openrndr.svg")).readText())
    val shapeNodes = svg.findShapes()
    class A(var x: Double, var y: Double, var scale: Double, var rotation: Double) : Animatable()

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
                it.animate("x", Math.random() * width/2.0 - width/4.0, 1500, Easing.QuadInOut)
                it.animate("y", Math.random() * height/2.0 - height/4.0, 1500, Easing.QuadInOut)
                it.animate("scale", Math.random()*1.0 + 0.5, 1500, Easing.QuadInOut)
                it.animate("rotation", Math.random() * 360.0 - 180.0, 1500, Easing.QuadInOut)
                it.complete()
                it.delay(1000)
                it.animate("dummy", 0.0, 0)
            }
        }
        val shapes = shapeNodes.mapIndexed { shapeIndex, it ->
            val c = it.bounds.center

            //val base = it.clockwise.sub(0.0, Math.cos(seconds+k*Math.PI/10.0)*0.5+0.5)
            val shape = it.shape.transform(transform {
                translate(anims[shapeIndex ].x, anims[shapeIndex ].y)
                translate(c)
                rotate(Vector3.UNIT_Z, anims[shapeIndex].rotation)
                scale(anims[shapeIndex ].scale)
                translate(-c)
            })

            // this is because the SVG reader is broken :(
            val contours = shape.contours.mapIndexed { index, it ->
                if (index == 0)
                    it.counterClockwise
                else {
                    it.clockwise
                }
            }
            Shape(contours)
        }

    drawer.background(ColorRGBa.BLACK)
    drawer.fill = ColorRGBa.PINK
    drawer.stroke = ColorRGBa.PINK
    drawer.strokeWeight = 2.0
    drawer.shapes(shapes)
    frames++
}
}