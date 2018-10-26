package demos

import org.openrndr.color.ColorRGBa
import org.openrndr.animatable.Animatable
import org.openrndr.color.mix
import org.openrndr.filter.blend.add
import org.openrndr.filter.blur.GaussianBlur
import org.openrndr.math.Vector2
import org.openrndr.math.map
import org.openrndr.workshop.toolkit.typography.Fonts
import poster


val DemoHashBlur: Demo = {
    val text = Text(drawer, "SP", Fonts.Rubik_Black, 1000.0)
    val text2 = Text(drawer, "AN", Fonts.Rubik_Black, 1000.0)

    class BlurParams(
        var radius: Double = 50.0
    ) : Animatable()

//    val blur = VelocityBlur().apply{
//        velocity = 200
//    };

    val blur = GaussianBlur();
    val blur2 = org.openrndr.filter.blur.BoxBlur()
    val blur3 = org.openrndr.filter.blur.HashBlur()

    val blurParams = BlurParams()
    fun animate() {
        blurParams.cancel()
        blurParams.delay(5000)
        blurParams.animate(blurParams::radius, 10.0, 10000)
        blurParams.complete {
            blurParams.delay(5000)
            blurParams.animate(blurParams::radius, 50.0, 10000)
            blurParams.complete {
                animate()
            }
        }
    }
    animate()
    fun() {
        val r = map(0.0, width.toDouble(), 0.0, 1.0, mouse.position.x)
        blurParams.updateAnimation()
        drawer.background(ColorRGBa.BLACK)
        blur3.apply {
            radius = blurParams.radius
            radius = r * 50.0
            time = seconds
            samples = 20
            gain = Math.max(1.0, (r * 2.0))
        }
//        blur2.apply {
//            window = 50
//            spread = 2.0
//            gain = 1.0
//        }
//        blur.apply {
//            gain = 1.0
//            spread = 10.0
//            window = 25
//            sigma = 5.0
//        }
        poster(drawer) {
            layer(post = blur3) {
                drawer.fill = mix(
                    ColorRGBa.BLUE,
                    ColorRGBa.RED,
                    0.5
                )
                text.draw(
                    Vector2(width / 2.0, height / 2.0),
                    Text.HorizontalAlign.CENTER,
                    Text.VerticalAlign.CENTER
                )
            }

            layer(post = blur3, blend = add) {
                drawer.fill = mix(
                    ColorRGBa.RED,
                    ColorRGBa.YELLOW,
                    0.5
                )
                text2.draw(
                    Vector2(width / 2.0, height / 2.0),
                    Text.HorizontalAlign.CENTER,
                    Text.VerticalAlign.CENTER
                )
            }
        }
    }
}

