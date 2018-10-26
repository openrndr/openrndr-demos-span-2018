package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.renderTarget
import org.openrndr.draw.shadeStyle


fun drawStuff(drawer: Drawer) {
    (0..drawer.width step 100).forEach { x ->
        (0..drawer.height step 100).forEach { y ->
            drawer.rectangle(x.toDouble(), y.toDouble(), 75.0, 75.0)
        }
    }
}

class Mask(val width: Int, val height: Int) {
    val rt by lazy {
        renderTarget(width, height) {
            colorBuffer()
        }
    }

    fun apply(drawer: Drawer, drawFn: Drawer.() -> Unit) {
        drawer.isolatedWithTarget(rt){
            drawer.background(ColorRGBa.BLACK)
            drawer.fill = ColorRGBa.GREEN
            drawFn()
        }
        drawer.shadeStyle = shadeStyle {
            fragmentTransform = """
                x_fill.rgba = vec4(0.0, 0.0, 0.0, 1.0 - x_fill.g);
            """.trimIndent()
        }
        drawer.image(rt.colorBuffer(0))
        drawer.shadeStyle = null
    }
}

// this is not a demo just a sandbox to work on masking
val Masking: Demo = {
    val mask = Mask(width, height)
    ({
        drawer.background(ColorRGBa.WHITE)
        drawer.fill = ColorRGBa.RED
        drawStuff(drawer)
        mask.apply(drawer) {
            drawer.circle(width / 2.0, height / 2.0, 500.0)
        }
    })
}