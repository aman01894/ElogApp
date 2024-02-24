package com.example.elogapp.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.lang.IllegalArgumentException

object ImageUtil {

    @Throws(IllegalArgumentException::class)

    fun convertBase64ToImage(base64Str: String): Bitmap? {
        val decodedBytes: ByteArray = Base64.decode(
            base64Str.substring(base64Str.indexOf(",") + 1),
            Base64.DEFAULT
        )
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    @JvmStatic
    fun convertBitmapToBase64(bitmap: Bitmap): String? {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }


}