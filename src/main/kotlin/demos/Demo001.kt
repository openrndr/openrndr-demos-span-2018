package demos

import org.openrndr.color.ColorRGBa

val Demo001: Demo = {
    var frames = 0
    {
        val size = Math.abs(Math.cos(seconds)) * 50.0
        drawer.background(ColorRGBa.YELLOW)
        drawer.fill = ColorRGBa.BLUE
        drawer.circle(width / 2.0, height / 2.0, size)
        frames++
    }
}

