package demos

import org.openrndr.Program
import DemoRunner



// a function which should contain draw commands
typealias DrawFunction = () -> Unit

// A demo is just a function with Program as its receiver (so it can access everything Program has),
// returning a draw function
typealias Demo = DemoRunner.() -> DrawFunction
