package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.math.Vector3
import org.openrndr.workshop.toolkit.typography.Fonts

val Mesh001: Demo = {

    var cylinderVertices = 0
    val cylinderMesh = vertexBuffer(vertexFormat {
        position(3)
        textureCoordinate(2)
    }, 2400).apply {
        cylinderVertices = put {
            cylinder(96, 4, 1.0, Math.PI*2.0) { position, texCoord ->
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
            drawer.fontMap = FontImageMap.fromUrl(Fonts.IBMPlexMono_Bold, 256.0)
            drawer.fill = ColorRGBa.PINK
            for (y in 0 until 5)
            drawer.text("OPENRNDR", 0.0, y * 256.0 - 10)


        }
        drawer.isolated {
            drawer.perspective(90.0, width * 1.0 / height, 0.1, 100.0)
            drawer.lookAt(Vector3(0.0, 0.0, -Math.PI), Vector3.ZERO, Vector3.UNIT_Y)
            drawer.rotate(Vector3.UNIT_Y, seconds*10.0)
            drawer.translate(0.0, 0.0, -Math.PI)


            drawer.depthWrite = true
            drawer.depthTestPass =  DepthTestPass.LESS_OR_EQUAL
            drawer.shadeStyle = shadeStyle {
                fragmentTransform = """
                    float ring = floor(va_texCoord0.x*4.0);
                    float ringOffset = cos(ring+p_seconds*4.0);
                    x_fill = texture(p_texture, mod(va_texCoord0.yx + vec2(p_seconds + ringOffset, 0.0), vec2(1.0)));
//                    x_fill.rgb = vec3(0);
//                    x_fill.r = va_texCoord0.x;
//                    //x_fill.g = va_texCoord0.y;

                """.trimIndent()
                parameter("seconds", seconds*0.1)
                parameter("texture", rt.colorBuffer(0))
            }

            drawer.vertexBuffer(cylinderMesh, DrawPrimitive.TRIANGLES, 0, cylinderVertices)
        }

    }
}

