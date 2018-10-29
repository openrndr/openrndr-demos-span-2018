package demos

import org.openrndr.Program
import org.openrndr.extensions.Debug3D


abstract class DemoRunner : Program(){
    // val camera = Debug3D()
    abstract var camera : Debug3D
}

// a function which should contain draw commands
typealias DrawFunction = () -> Unit

// A demo is just a function with Program as its receiver (so it can access everything Program has),
// returning a draw function
typealias Demo = DemoRunner.() -> DrawFunction
