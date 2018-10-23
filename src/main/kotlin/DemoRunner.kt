import demos.*
import org.openrndr.Program
import org.openrndr.application
import org.openrndr.configuration
import org.openrndr.extensions.FunctionDrawer

class DemoRunner : Program() {

    override fun setup() {


        // list of demo entries:
        // the number of seconds a demo should run paired with the demo itself
        val demos = listOf(
            1 to Demo001,
            1 to Demo002,
            1 to Demo003
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


        fun initDemo(entry: Pair<Int, Demo>): CurrentRunning {
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

        extend(FunctionDrawer {
            if (!iterator.hasNext()) {
                iterator = demos.iterator()
            }
            if (currentRunning.shouldKill()) {
                currentRunning = initDemo(iterator.next())
            }
            currentRunning.drawFn()
        })

    }
}

fun main(args: Array<String>) {
    application(DemoRunner(), configuration {
        width = 1920
        height = 1080
    })
}