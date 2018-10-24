package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.math.Vector2
import org.openrndr.math.transforms.rotateZ
import org.openrndr.shape.LineSegment
import org.openrndr.workshop.toolkit.typography.Fonts

val Distance002: Demo = {

    val rt = renderTarget(1920 / 8, 1080 / 8) {
        colorBuffer()
    }

    println(rt.colorBuffer(0).height)
    var frames = 0
    {
        drawer.isolated {
            drawer.isolatedWithTarget(rt) {
                drawer.background(ColorRGBa.BLACK)
                drawer.fontMap = FontImageMap.fromUrl(Fonts.IBMPlexMono_Bold, 512.0)
                drawer.fill = ColorRGBa.WHITE
                for (y in 0 until 4) {
                    drawer.text("OPENRNDR".let { it.substring(0, ((1 + it.length) * (Math.cos(y + seconds) * 0.5 + 0.5)).toInt()) }, 0.0, y * 400.0)
                }
            }
            rt.colorBuffer(0).shadow.download()
            val distance = jumpFlooder.distanceToContour(drawer, rt.colorBuffer(0))
            distance.shadow.download()
            val directions = jumpFlooder.result.shadow.flatMapIndexed(
                    (0 until jumpFlooder.width step 2),
                    (0 until jumpFlooder.height step 2)
            ) { x, y, r, g, _, _ ->

                val dist = Vector2(r, g)
                val direction = (rotateZ(dist.length  * 4.0 + seconds * 90.0) * dist.xy01).xyz.xy.normalized * 8.0
                if (direction.length > 0.0 && rt.colorBuffer(0).shadow[x, (1080 / 8) - 1 - y].r < 1.0)
                    LineSegment(x * 8.0, height - y * 8.0, x * 8.0 + direction.x, height - y * 8.0 + direction.y)
                else
                    null
            }.filterNotNull()
            drawer.background(ColorRGBa.PINK)
            drawer.stroke = ColorRGBa.BLACK
            drawer.strokeWeight = 2.0
            drawer.lineCap = LineCap.ROUND
            drawer.lineSegments(directions.flatMap { listOf(it.start, it.end) })
        }
    }
}

