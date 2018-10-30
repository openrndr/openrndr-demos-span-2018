package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.animatable.Animatable
import org.openrndr.color.ColorRGBa.Companion.PINK
import org.openrndr.math.Vector2
import org.openrndr.math.map
import org.openrndr.workshop.toolkit.typography.Fonts
import poster


val DemoHashBlur: Demo = {
    val texts = listOf(
        Text(drawer, "OPEN", Fonts.Rubik_Black, 500.0),
        Text(drawer, "RNDR", Fonts.Rubik_Black, 500.0)
    )

    var index = 0

    class BlurParams(
        var radius: Double = 50.0,
        var gain: Double = 3.5
    ) : Animatable()

    val blur = org.openrndr.filter.blur.HashBlur()

    val blurParams = BlurParams()
    fun animate() {
        blurParams.cancel()
        blurParams.delay(2000)
        blurParams.animate(blurParams::radius, 5.0, 500)
        blurParams.animate(blurParams::gain, 1.0, 500)
        blurParams.complete {
            blurParams.delay(2000)
            blurParams.animate(blurParams::radius, 50.0, 500)
            blurParams.animate(blurParams::gain, 3.5, 500)
            blurParams.complete {
                index = (index + 1) % texts.size
                animate()
            }
        }
    }
    animate()
    fun() {
        val r = map(0.0, width.toDouble(), 0.0, 1.0, mouse.position.x)
        blurParams.updateAnimation()
        drawer.background(ColorRGBa.BLACK)
        blur.apply {
            radius = blurParams.radius
            time = seconds
            samples = 20
            gain = blurParams.gain
        }
        poster(drawer) {
            layer(post = blur) {
                drawer.fill = PINK
                texts[index].draw(
                    Vector2(width / 2.0, height / 2.0),
                    Text.HorizontalAlign.CENTER,
                    Text.VerticalAlign.CENTER
                )
            }
        }
    }
}

