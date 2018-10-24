import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.FontImageMap
import org.openrndr.math.Vector2
import org.openrndr.text.Writer


// a  simple wrapper around openrndr's text drawing tools
// providing the additional feature of alignment to various horizontal and vertical anchors

class Text(val drawer: Drawer,
           val text: String,
           url: String,
           size: Double
) {
    private val font = FontImageMap.fromUrl(url, size)
    val writer = Writer(drawer)
    private val width = font.let {
        drawer.fontMap = font
        writer.textWidth(text)
    }

    enum class HorizontalAlign {
        LEFT,
        CENTER,
        RIGHT
    }

    enum class VerticalAlign {
        ASCENDER,
        XHEIGHT,
        BASELINE,
        DESCENDER,
        CENTER_XHEIGHT,
        CENTER_FULL,
        CENTER_ASCENDER,
        ;
    }

    fun align(horizontalAlign: HorizontalAlign, verticalAlign: VerticalAlign): Vector2 {
        val deltaX = when (horizontalAlign) {
            HorizontalAlign.LEFT -> 0.0
            HorizontalAlign.CENTER -> -width / 2.0
            HorizontalAlign.RIGHT -> -width
        }
        val deltaY = when (verticalAlign) {
            VerticalAlign.BASELINE -> 0.0
            VerticalAlign.ASCENDER -> font.ascenderLength
            VerticalAlign.XHEIGHT -> font.height
            /// Not sure if we need all these vertical alignment options
            VerticalAlign.CENTER_XHEIGHT -> font.height / 2.0
            VerticalAlign.CENTER_ASCENDER -> font.ascenderLength / 2.0
            VerticalAlign.CENTER_FULL -> ((font.ascenderLength + font.descenderLength) / 2.0)
            VerticalAlign.DESCENDER -> font.descenderLength
        }
        return Vector2(deltaX, deltaY)
    }


    fun draw(
        position: Vector2,
        horizontalAlign: HorizontalAlign = HorizontalAlign.LEFT,
        verticalAlign: VerticalAlign = VerticalAlign.BASELINE
    ) {
        drawer.fontMap = font
        val alignment = align(horizontalAlign, verticalAlign)
        drawer.text(text, position + alignment)
    }

    fun explain(
        position: Vector2,
        horizontalAlign: HorizontalAlign = HorizontalAlign.LEFT,
        verticalAlign: VerticalAlign = VerticalAlign.BASELINE
    ) {

        drawer.fontMap = font
        val alignment = align(horizontalAlign, verticalAlign)
        val p = position + alignment

        // LEADING
        drawer.fill = ColorRGBa.PINK
        drawer.rectangle(p - Vector2(0.0, font.leading), width, font.leading)

        // ASC
        drawer.fill = ColorRGBa.BLUE
        drawer.rectangle(p - Vector2(0.0, font.ascenderLength), width, font.ascenderLength)

        // BODY
        drawer.fill = ColorRGBa.GRAY
        drawer.rectangle(p - Vector2(0.0, font.height), width, font.height)

        // DESC
        drawer.fill = ColorRGBa.BLUE
        drawer.rectangle(p, width, -font.descenderLength)

        drawer.fill = ColorRGBa.BLACK
        drawer.text(text, p)
    }

}

