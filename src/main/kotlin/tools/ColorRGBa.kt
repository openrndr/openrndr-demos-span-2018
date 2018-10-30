package tools

import org.openrndr.color.ColorRGBa


fun ColorRGBa.invert(): ColorRGBa {
    return copy(
        r = 1.0 - this.r,
        g = 1.0 - this.g,
        b = 1.0 - this.b,
        a = this.a
    )
}