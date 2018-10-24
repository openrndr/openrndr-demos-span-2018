package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.math.Vector2
import org.openrndr.math.transforms.rotateZ
import org.openrndr.math.transforms.transform
import org.openrndr.shape.LineSegment
import org.openrndr.workshop.toolkit.typography.Fonts

val Distance005: Demo = {

    val rt = renderTarget(1920 / 8, 1080 / 8) {
        colorBuffer()
    }

    var frames = 0
    {
        drawer.isolated {
            drawer.isolatedWithTarget(rt) {
                drawer.background(ColorRGBa.BLACK)
                drawer.fontMap = FontImageMap.fromUrl(Fonts.IBMPlexMono_Bold, 512.0)
                drawer.fill = ColorRGBa.WHITE
                for (y in 0 until 4) {
                    drawer.text("OPENRNDR".let { it.substring(0, ((1 + it.length) * (Math.cos(y + seconds) * 0.5 + 0.5)).toInt()) }, 0.0, y * 400.0 - 130.0)
                }
            }
            rt.colorBuffer(0).shadow.download()
            val distance = jumpFlooder.distanceToContour(drawer, rt.colorBuffer(0))
            distance.shadow.download()

            val center = Vector2(width/2.0 + Math.cos(seconds)*width/4.0, height/2.0 + Math.sin(seconds)*height/4.0)

            val directions = jumpFlooder.result.shadow.flatMapIndexed(
                    (0 until jumpFlooder.width step 2),
                    (0 until jumpFlooder.height step 2)
            ) { x, y, r, g, _, _ ->



                val realDist = Vector2(r, g)
                val fakeDist = Vector2(x*8.0, y * 8.0) - center
                val dist = realDist.normalized * Math.exp(-fakeDist.length*0.005)*500.0
                val direction =  rotateZ(seconds*90.0) * (dist / 8.0).xy01

                if (direction.length > 0.0 && rt.colorBuffer(0).shadow[x, (1080 / 8) - 1 - y].r < 1.0)
                    LineSegment(x * 8.0, height - y * 8.0, x * 8.0 + direction.x, height - y * 8.0 + direction.y)
                else
                    null
            }.filterNotNull()
            drawer.background(ColorRGBa.BLACK)
            drawer.stroke = ColorRGBa.PINK
            drawer.strokeWeight = 2.0
            drawer.lineCap = LineCap.ROUND
            drawer.lineSegments(directions.flatMap { listOf(it.start, it.end) })
        }
    }
}

