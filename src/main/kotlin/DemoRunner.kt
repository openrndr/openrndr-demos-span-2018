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
            15 to Distance001,
            15 to Distance002,
            15 to Distance003,
            15 to Distance004,
            15 to Distance005,
            15 to Mesh001,
            15 to Mesh002,
            15 to Mesh003,
            15 to Mesh004,
            15 to Gradient003,
            15 to Gradient004,
            15 to Type3d,
            15 to DemoHashBlur,
            15 to DemoHashBlur2,
            15 to DemoHashBlur3,
            15 to DemoGradient3D,
            15 to Mozaic,
            15 to Flag,
            15 to Vector001,
            15 to Vector002,
            15 to Vector003,
            15 to Vector004,
            15 to Vector005,
            15 to Vector006,
            15 to Vector007,
            15 to Vector008,
            15 to Vector009,
            15 to Vector010,
            15 to Vector011,
            15 to Vector012,
            15 to Vector013,
            15 to Stacker001,
            15 to Stacker002,
            15 to Stacker003,
            15 to Moire001,
            15 to Moire002,
            15 to Moire003
        ).shuffled()

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
