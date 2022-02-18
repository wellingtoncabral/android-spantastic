package com.wcabral.spantastic

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun Resources.decodeSampledBitmapFromResource(
    resId: Int,
    reqWidth: Int,
    reqHeight: Int
): Bitmap {
    // First decode with inJustDecodeBounds=true to check dimensions
    return BitmapFactory.Options().run {
        inJustDecodeBounds = true
        BitmapFactory.decodeResource(this@decodeSampledBitmapFromResource, resId, this)

        // Calculate inSampleSize
        inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        inJustDecodeBounds = false

        BitmapFactory.decodeResource(this@decodeSampledBitmapFromResource, resId, this)
    }
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}
