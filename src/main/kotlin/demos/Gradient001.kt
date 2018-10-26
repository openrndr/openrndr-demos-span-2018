package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.shadeStyle
import org.openrndr.shape.Rectangle
import org.openrndr.workshop.toolkit.typography.Fonts


val Gradient001: Demo = {
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
                        float mi = p_time * 5.0 + m * 5.0;
                        m = (cos(mi) + 1.0 ) * 0.5;
                    } else {
                        float mi = p_time * 5.0 + m * 25.0;
                        m = (cos(mi) + 1.0 ) * 0.5;
                    }
                    vec3 finalColor = mix(p_c1.rgb, p_c2.rgb, m);
                    x_fill.rgb = finalColor;
                    """
    }

    val c1 = ColorRGBa.BLACK
    val c2 = ColorRGBa.BLUE
    gradient.parameter("c1", c1)
    gradient.parameter("c2", c2)

    ({
        gradient.parameter("time", seconds)
        gradient.parameter("reverse", 0)
        drawer.stroke = null
        drawer.shadeStyle = gradient
        drawer.rectangle(0.0, 0.0, width.toDouble(), height.toDouble())
        drawer.translate(0.0, -20.0)
        gradient.parameter("reverse", 1)
        text.stretch(
            Rectangle(0.0, 0.0, width.toDouble(), height.toDouble())
        ) {
            drawer.fill = ColorRGBa.BLACK
        }
    })
}

