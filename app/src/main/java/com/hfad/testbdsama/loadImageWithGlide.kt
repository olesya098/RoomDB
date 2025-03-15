package com.hfad.testbdsama
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

suspend fun loadImageWithGlide(context: Context, uri: Uri): ByteArray? {
    return withContext(Dispatchers.IO) {
        try {
            // Загружаем изображение с помощью Glide
            val bitmap = Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(RequestOptions().fitCenter())
                .submit()
                .get()

            // Конвертируем Bitmap в ByteArray
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}