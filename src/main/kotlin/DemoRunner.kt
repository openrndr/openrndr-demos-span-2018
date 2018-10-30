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
            10 to Distance001,
            10 to Distance002,
            10 to Distance003,
            10 to Distance004,
            10 to Distance005,
            10 to Mesh001,
            10 to Mesh002,
            10 to Mesh003,
            10 to Mesh004,
            10 to Gradient003,
            30 to Gradient004,
            10 to Type3d,
            10 to DemoHashBlur,
            10 to DemoHashBlur2,
            10 to DemoHashBlur3,
            10 to DemoGradient3D,
            10 to Mozaic,
            10 to Flag,
            10 to Vector001,
            10 to Vector002,
            10 to Vector003,
            10 to Vector004,
            10 to Vector005,
            10 to Vector006,
            10 to Vector007,
            10 to Vector008,
            10 to Vector009,
            10 to Vector010,
            10 to Vector011,
            10 to Vector012,
            10 to Vector013,
            10 to Stacker001,
            10 to Stacker002,
            10 to Stacker003,
            10 to Moire001,
            10 to Moire002,
            10 to Moire003
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


        var index = 0
        var currentRunning: CurrentRunning = initDemo(demos.first())
        keyboard.keyUp.listen { it ->
            val nextIndex = if (it.scanCode == 124) {
                (index + 1) % demos.size
            } else if (it.scanCode == 123) {
                val maybeNext = index - 1
                if (maybeNext < 0) {
                    demos.size - 1
                } else {
                    maybeNext
                }
            } else {
                null
            }
            nextIndex?.let { it ->
                currentRunning = initDemo(demos[it])
                index = it
            }
        }

        extend(camera)
        extend(FunctionDrawer {
            if (currentRunning.shouldKill()) {
                index = (index + 1) % demos.size
                currentRunning = initDemo(demos[index])
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
