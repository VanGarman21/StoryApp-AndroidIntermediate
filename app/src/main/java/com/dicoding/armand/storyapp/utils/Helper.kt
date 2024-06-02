package com.dicoding.armand.storyapp.utils


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

fun generateTimeStamp(): String {
    val formatter = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
    return formatter.format(Date())
}

fun createTempImageFile(context: Context): File {
    val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(generateTimeStamp(), ".jpg", directory)
}

fun convertUriToFile(selectedImg: Uri, context: Context): File {
    val resolver = context.contentResolver
    val tempFile = createTempImageFile(context)

    resolver.openInputStream(selectedImg)?.use { inputStream ->
        FileOutputStream(tempFile).use { outputStream ->
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
        }
    } ?: throw IOException("Unable to open input stream from URI")

    return tempFile
}

fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var quality = 100
    var isCompressed = false

    while (quality > 0 && !isCompressed) {
        ByteArrayOutputStream().use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            if (outputStream.size() <= 1_000_000) {
                FileOutputStream(file).use { fileOutputStream ->
                    fileOutputStream.write(outputStream.toByteArray())
                }
                isCompressed = true
            }
        }
        if (!isCompressed) {
            quality -= 5
        }
    }

    return file
}
