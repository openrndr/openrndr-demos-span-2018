package tools

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.filter.filterShaderFromUrl
import org.openrndr.resourceUrl


class MaskFilter : Filter(filterShaderFromUrl(resourceUrl("/Mask.frag")))

class Mask(val drawer: Drawer) {
    private val maskFilter = MaskFilter()
    private val result = colorBuffer(drawer.width, drawer.height)
    val rt by lazy {
        renderTarget(drawer.width, drawer.height) {
            colorBuffer()
        }
    }

    fun applyTo(source: RenderTarget, drawMask: Drawer.() -> Unit): ColorBuffer {
        drawer.isolatedWithTarget(rt) {
            drawer.background(ColorRGBa.BLACK)
            drawer.fill = ColorRGBa.WHITE
            drawMask()
        }
        maskFilter.apply(
            listOf(
                source.colorBuffer(0),
                rt.colorBuffer(0)
            ).toTypedArray()
            , result
        )
        return result
    }
}

