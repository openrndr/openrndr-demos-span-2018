package demos

import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.filter.blur.HashBlur
import org.openrndr.math.Vector2
import org.openrndr.workshop.toolkit.typography.Fonts
import poster
import java.io.File


data class C(val position: Vector2, val radius: Double) {
    companion object {
        fun grid(c: Pair<Double, Int>, width: Int, height: Int): List<C> {
            val (radius, step) = c
            return (0..width step step).flatMap { x ->
                (0..height step step).map { y ->
                    C(Vector2(x.toDouble(), y.toDouble()), radius)
                }
            }
        }
    }
}

fun loadConfigs(): List<List<Pair<Double, Int>>> {
    return File("data/Moire001.txt").readLines().map { line ->
        line.split(":").map { l ->
            l.split("&").let { p ->
                val radius = p[0].toDouble()
                val step = p[1].toInt()
                Pair(radius, step)
            }
        }
    }
}

fun saveConfig(config: List<Pair<Double, Int>>) {
    File("data/Moire001.txt").appendText(
        "\n" + config.map {
            "${it.first}&${it.second}"
        }.joinToString(":")
    )
    println("saved config")
}

class State(val program: Program) {
    private val configs = loadConfigs()
    private var config = configs[Math.floor(Math.random() * configs.size).toInt()]
    var layers = config.map { it -> C.grid(it, program.width, program.height) }

    init {
        program.keyboard.keyDown.filter { it.name == "s" }.listen {
            saveConfig(config)
        }

        program.keyboard.keyDown.filter { it.name == "r" }.listen {
            val i = Math.floor(Math.random() * configs.size).toInt()
            println("i: ${i}")
            layers = configs[i].map { it -> C.grid(it, program.width, program.height) }
        }

        program.mouse.clicked.listen {
            layers = makeLayers()
        }
    }

    private fun makeLayers(): List<List<C>> {
        config = listOf(Pair(10.0, 30)) + List(nLayers) {
            val radius = 5.0 + Math.random() * 50.0
            val step = (radius * 2.0 + 1.0 * Math.random() * 50.0).toInt()
            Pair(radius, step)
        }
        return config.map { it -> C.grid(it, program.width, program.height) }
    }
}


const val nLayers = 3

val Moire001: Demo = {
    val texts = listOf(
        "OPEN",
        "RNDR"
    ).map {
        Text(drawer, it, Fonts.Rubik_Black, 500.0)
    }

    val state = State(this)

    val blur = HashBlur()
    val colors = listOf(
        ColorRGBa.BLACK,
        ColorRGBa.WHITE
    )

    ({
        drawer.stroke = null

        poster(drawer) {
            drawer.background(ColorRGBa.BLACK)
            layer(post = listOf(
                blur.apply { time = seconds }
            )) {
                drawer.fill = ColorRGBa.BLACK
                state.layers.forEachIndexed { index, layer ->
                    val ps = layer.map { it.position }
                    val radius = layer[0].radius * (Math.abs(Math.cos(seconds + index))) + 2.0
                    drawer.fill = colors[index % colors.size]
                    drawer.circles(ps, radius)
                }
            }

            layer {
                drawer.fill = ColorRGBa.BLACK
                texts.forEachIndexed { index, text ->
                    text.draw(Vector2(width / 2.0 - (if (index == 0) 10.0 else 0.0),
                        height / 2.0 - if (index == 0) {
                            (text.height / 2.0) + 10.0
                        } else {
                            -(text.height / 2.0 + 10.0)
                        }
                    ),
                        Text.HorizontalAlign.CENTER,
                        Text.VerticalAlign.CENTER
                    )
                }
            }
        }
    })
}


