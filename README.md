# OpenRNDR Demos Span 2018


## Development
Make a private file `Sandbox.kt` with the following:
```
import demos.MyDemo
import org.openrndr.Program
import org.openrndr.application
import org.openrndr.configuration
import org.openrndr.extensions.FunctionDrawer


class Sandbox : Program() {
    override fun setup() {
        val demo = MyDemo(this)
        extend(FunctionDrawer {
            demo()
        })
    }
}


fun main(args: Array<String>) {
    application(Sandbox(), configuration {
        width = 1920
        height = 1080
    })
}
```
Replace `MyDemo` for the demo you're working on.