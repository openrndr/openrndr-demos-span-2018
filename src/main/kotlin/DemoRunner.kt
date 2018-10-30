import demos.*
import org.openrndr.application
import org.openrndr.configuration
import org.openrndr.draw.isolated
import org.openrndr.extensions.Debug3D
import org.openrndr.extensions.FunctionDrawer
import org.openrndr.math.Matrix44


class Runner : DemoRunner() {
    override lateinit var camera: Debug3D

    override fun setup() {
        camera = Debug3D()
        // list of demo entries:
        // the number of seconds a demo should run paired with the demo itself
        val demos = listOf(
//            1 to Demo001,
//            10 to Distance001,
//            10 to Distance002,
//            10 to Distance003,
//            10 to Distance004,
//            10 to Distance005,
                10 to Type3d,
                10 to DemoHashBlur,
                10 to DemoHashBlur2,
                10 to DemoHashBlur3,
                10 to DemoHashBlur4,
                10 to DemoGradient3D,
                10 to Mozaic,
                10 to Flag,

//            10 to Vector001,
//            10 to Vector002,
//            10 to Vector003,
//            10 to Vector004,
                10 to Gradient001,
                10 to Gradient002,
                10 to Gradient003,
                10 to Gradient004,
                10 to Stacker001,
                10 to Stacker002,
                10 to Stacker003,
                10 to Moire001,
                10 to Moire002
        )

        class CurrentRunning(
                val started: Long,
                val duration: Int,
                val drawFn: DrawFunction
        ) {
            fun shouldKill(): Boolean {
                return started + (duration * 1000) < System.currentTimeMillis()
            }
        }

        fun resetCamera() {
            camera.enabled = false
            drawer.view = Matrix44.IDENTITY
            drawer.ortho()
        }


        fun initDemo(entry: Pair<Int, Demo>): CurrentRunning {
            resetCamera()
            val (duration, demo) = entry
            val drawDemo = demo(this)
            return CurrentRunning(
                    started = System.currentTimeMillis(),
                    duration = duration,
                    drawFn = drawDemo
            )
        }


        var iterator = demos.iterator()
        var currentRunning: CurrentRunning = initDemo(iterator.next())

        mouse.clicked.listen {
            currentRunning = initDemo(iterator.next())
        }
        extend(camera)
        extend(FunctionDrawer {
            if (!iterator.hasNext()) {
                iterator = demos.iterator()
            }
            if (currentRunning.shouldKill()) {
                currentRunning = initDemo(iterator.next())
            }
            drawer.isolated {
                currentRunning.drawFn()
            }
        })

    }
}

fun main(args: Array<String>) {
    application(Runner(), configuration {
        width = 1920
        height = 1080
    })
}