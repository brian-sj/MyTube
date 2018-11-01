package com.goodmorningvoca.std.app.ui

import android.graphics.*
import android.graphics.Bitmap.Config

// enables hardware accelerated rounded corners
// original idea here : http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/
// radius is corner radii in dp
// margin is the board in dp

class RoundedTransformation (private val radius: Int, private val margin: Int  // dp
) : com.squareup.picasso.Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val output = Bitmap.createBitmap(source.width, source.height, Config.ARGB_8888)
        val canvas = Canvas(output)
        canvas.drawRoundRect(RectF(margin.toFloat(), margin.toFloat(), (source.width - margin).toFloat(), (source.height - margin).toFloat()), radius.toFloat(), radius.toFloat(), paint)

        val paint1 = Paint()
        paint1.setColor( Color.GRAY)
        paint1.style = Paint.Style.STROKE
        paint1.isAntiAlias = true
        //paint1.strokeWidth = 1
        canvas.drawRoundRect(RectF(margin.toFloat(), margin.toFloat(), (source.width - margin).toFloat(), (source.height - margin).toFloat()), radius.toFloat(), radius.toFloat(), paint1)

        if (source != output) {
            source.recycle()
        }

        return output
    }

    override fun key(): String {
        return "rounded"
    }
}