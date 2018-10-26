package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.math.Vector3
import org.openrndr.workshop.toolkit.typography.Fonts

val Mesh004: Demo = {

    var cylinderVertices = 0
    val cylinderMesh = vertexBuffer(vertexFormat {
        position(3)
        textureCoordinate(2)
    }, 2400).apply {
        cylinderVertices = put {
            cylinder(96, 4, 1.0, Math.PI * 2.0) { position, texCoord ->
                write(position)
                write(texCoord)
            }
        }
    }

    val rt = renderTarget(1024, 1024) {
        colorBuffer()
    }

    var frames = 0
    {

        drawer.isolatedWithTarget(rt) {
            drawer.ortho(rt)
            drawer.background(ColorRGBa.BLACK)

            drawer.stroke = ColorRGBa.WHITE
            drawer.strokeWeight = 16.0
//            drawer.lineSegment(0.0, 0.0, 1024.0, 1024.0)
            drawer.fontMap = FontImageMap.fromUrl(Fonts.IBMPlexMono_Bold, 128.0)
            drawer.fill = ColorRGBa.PINK
            for (y in 0 until 10)
                drawer.text("OPENRNDR", 0.0, y * 128.0 - 10)


        }
        drawer.isolated {
            val r = width.toDouble()/height
            //drawer.perspective(90.0, width * 1.0 / height, 0.1, 100.0)
            drawer.ortho(-3.0*r,3.0*r,-3.0, 3.0, 0.0, 100.0)
            drawer.lookAt(Vector3(0.0, 0.0, -Math.PI), Vector3.ZERO, Vector3.UNIT_Y)
            drawer.rotate(Vector3.UNIT_Z, 180.0)
            drawer.rotate(Vector3.UNIT_X, 90.0)

            drawer.translate(0.0, 0.0, -Math.PI)


            drawer.depthWrite = true
            drawer.depthTestPass = DepthTestPass.LESS_OR_EQUAL
            drawer.shadeStyle = shadeStyle {
                fragmentTransform = """
                    float ring = va_texCoord0.x;
                    float ringOffset = ring*cos(p_seconds)*4.0;
                    x_fill = texture(p_texture, mod(va_texCoord0.yx + vec2(p_seconds + ringOffset, 0.0), vec2(1.0)));
                """.trimIndent()
                parameter("seconds", seconds * 0.1)
                parameter("texture", rt.colorBuffer(0))
            }

            drawer.translate(-4.0, 0.0, 0.0)
            for (i in 0 .. 4) {
                drawer.vertexBuffer(cylinderMesh, DrawPrimitive.TRIANGLES, 0, cylinderVertices)
                drawer.shadeStyle?.parameter("seconds", seconds * 0.4+i*Math.PI/8)
                drawer.translate(2.0, 0.0, 0.0)

            }
        }

    }
}

