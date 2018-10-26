package demos

import org.openrndr.math.Vector2
import org.openrndr.math.Vector3

fun cylinder(sides: Int, segments: Int, radius: Double, length: Double, vertexWriter: (Vector3, Vector2) -> Unit) {


    val dphi = (Math.PI * 2) / sides

    for (segment in 0 until segments) {

        for (side in 0 until sides) {

            val x0 = Math.cos(side * dphi)
            val x1 = Math.cos(side * dphi + dphi)
            val y0 = Math.sin(side * dphi)
            val y1 = Math.sin(side * dphi + dphi)

            val z0 = (length / segments) * segment
            val z1 = (length / segments) * (segment + 1)


            val u0 = (segment + 0.0) / segments
            val u1 = (segment + 1.0) / segments
            val v0 = (side + 0.0) / sides
            val v1 = (side + 1.0) / sides


            vertexWriter(Vector3(x0, y0, z0), Vector2(u0, v0))
            vertexWriter(Vector3(x0, y0, z1), Vector2(u1, v0))
            vertexWriter(Vector3(x1, y1, z1), Vector2(u1, v1))

            vertexWriter(Vector3(x1, y1, z1), Vector2(u1, v1))
            vertexWriter(Vector3(x1, y1, z0), Vector2(u0, v1))
            vertexWriter(Vector3(x0, y0, z0), Vector2(u0, v0))

        }


    }

}