package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.shadeStyle
import org.openrndr.shape.Rectangle
import org.openrndr.workshop.toolkit.typography.Fonts


val Gradient003: Demo = {
    val txt = "OPENRNDR"
    val text = Text(drawer, txt, Fonts.SpaceMono_Bold, width.toDouble())

    val gradient = shadeStyle {
        fragmentTransform = """
                    float m;
                    if(p_reverse == 0){
                       m = 1.0 -  va_position.y;
                    } else {
                       m = va_position.y;
                    }

                    if(p_reverse == 0){
                        float mi = p_time * 1.0 + m * 5.0 * p_offset;
                        m = (cos(mi) + 1.0 ) * 0.5;
                    } else {
                        float mi = p_time * 5.0 + m * p_offset;
                        m = (cos(mi) + 1.0 ) * 0.5;
                    }
                    vec3 finalColor = mix(p_c1.rgb, p_c2.rgb, m);
                    x_fill.rgb = finalColor * x_fill.a;
                    """
    }

    val c1 = ColorRGBa.BLACK
    val c2 = ColorRGBa.PINK
    gradient.parameter("c1", c1)
    gradient.parameter("c2", c2)

    ({
        gradient.parameter("time", seconds)
        gradient.parameter("reverse", 0)
        gradient.parameter("offset", 0.0)
        drawer.stroke = null
        drawer.background(c1)
        drawer.translate(25.0, -20.0)

        drawer.shadeStyle = gradient
        gradient.parameter("reverse", 1)
        List(10) { i ->
            gradient.parameter("offset", (i + 1).toDouble())
            text.stretch(
                Rectangle(i * 10.0, i * 10.0, width.toDouble() * 0.9, height.toDouble() * 0.9)
            ) {
                drawer.fill = ColorRGBa.BLACK
            }
        }
    })
}

