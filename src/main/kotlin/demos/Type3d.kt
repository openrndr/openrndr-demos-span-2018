package demos


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.openrndr.KeyboardModifier
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.extensions.Debug3D
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.math.transforms.transform
import org.openrndr.shape.Circle
import org.openrndr.workshop.toolkit.typography.Fonts
import java.io.File
import com.google.gson.reflect.TypeToken
import kotlin.math.sin

class CameraState(var handle: Int, var eye: Vector3, var lookAt: Vector3)

class CameraPositionsList (
        var cameraPositions: ArrayList<CameraState>
)

lateinit var cameraPositionsList: CameraPositionsList


val Type3d: Demo = {

    camera.enabled = true

    lateinit var projection: VertexBuffer
    lateinit var rt: RenderTarget

    rt = renderTarget(width, height) {
        colorBuffer()
    }

    drawer.isolatedWithTarget(rt) {
        drawer.background(ColorRGBa.BLACK)
        drawer.fill = ColorRGBa.WHITE
        drawer.fontMap = FontImageMap.fromUrl(Fonts.IBMPlexMono_Bold, 300.0)
        drawer.text("HELSINKI", width/2.0 - 540.0, height/2.0 + 100)
    }

    camera.orbitalCamera.rotateTo(
            Vector3(0.0, 0.0, 500.0)
    )

    projection = vertexBuffer(vertexFormat {
        attribute("transform", VertexElementType.MATRIX44_FLOAT32)
        attribute("color", VertexElementType.VECTOR4_FLOAT32)
    }, 50000)

    var handle = 0

    // GET SAVED CAMERA STATES
    cameraPositionsList = CameraPositionsList(ArrayList())

    GsonBuilder().create().fromJson( File("data/cameraPositions.json").readText(), CameraPositionsList::class.java).apply {
        if (this != null) {
            cameraPositionsList.cameraPositions = this.cameraPositions
        }
    }

    keyboard.keyDown.listen { it ->

        if(KeyboardModifier.SHIFT in it.modifiers) {
            if(it.name != "<null>") {
                if (it.name.toInt() in 0..9) {
                    println("Save camera at key " + it.name)
                    val handle = it.name.toInt()

                    val selection = cameraPositionsList.cameraPositions.filter { it.handle == handle }

                    cameraPositionsList.cameraPositions.add(
                            CameraState(
                                    it.name.toInt(),
                                    Vector3.fromSpherical(camera.orbitalCamera.spherical),
                                    camera.orbitalCamera.lookAt
                            ))

                    println(cameraPositionsList.cameraPositions.get(0).eye)
                    File("data/cameraPositions.json").writeText( Gson().toJson(cameraPositionsList) )
                }
            }
        } else {
            if (it.name in mutableListOf<String>("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")) {
                val handle = it.name.toInt()
                val selection = cameraPositionsList.cameraPositions.filter { it.handle == handle }.last()
                if(selection!=null) {
                    // println("Show camera at key " + handle)
                    camera.orbitalCamera.rotateTo(
                            eye = selection.eye
                    )
                    camera.orbitalCamera.panTo(
                            selection.lookAt
                    )
                }
            }
        }
    }

    projection.put {

        val cbs = rt.colorBuffer(0).shadow
        cbs.download()

        var pointCount = 0
        while (pointCount < 50000) {

            val target = Vector2((Math.random()*width), (Math.random()*height))
            val c = cbs.get(target.x.toInt(), target.y.toInt())

            var z = -15 + Math.random() * 30.0

            if(c.r > 0.5) {
                write(
                        transform {
                            translate(Vector3(target.x - width/2.0, height/2.0 - target.y, z))
                            rotate(Vector3(0.0, 1.0, 0.0), Math.random() * 360.0)
                            rotate(Vector3(1.0, 0.0, 0.0), Math.random() * 360.0)
                            rotate(Vector3(0.0, 0.0, 1.0), Math.random() * 360.0)
                            scale(1.0, Math.random()*3.0, 1.0)

                        })
                write( ColorRGBa.WHITE.opacify(0.5))
                pointCount++
            }

        }

    }


    var timer = System.currentTimeMillis();

    {
        drawer.background(ColorRGBa.BLACK)

        drawer.isolated {
            fill = ColorRGBa.BLACK
            stroke = null
            drawer.rotate(Vector3.UNIT_X, 90.0)

            drawer.shadeStyle = shadeStyle {
                vertexTransform = """
                        x_modelMatrix = i_transform;
                    """.trimIndent()
                fragmentTransform = """
                        x_fill = vi_color;
                    """.trimIndent()
                attributes(projection)
            }

            drawer.stroke = null
            drawer.fill = ColorRGBa.WHITE

            if(System.currentTimeMillis()-timer > 1500) {

                val selection = cameraPositionsList.cameraPositions.filter { it.handle == (handle%9) }.last()
                if(selection!=null) {
                    println("Show camera at key " + handle)
                    camera.orbitalCamera.rotateTo(
                            eye = selection.eye
                    )
                    camera.orbitalCamera.panTo(
                            selection.lookAt
                    )
                }
                handle++

                timer = System.currentTimeMillis()
            }


            for(i in 0..10) {

                drawer.circles((0 until 50000).map { Circle(Vector2(-1.0 / 2, -1.0 / 2 + ( i * 20.0)), 2.0) })

            }


        }
    }
}

