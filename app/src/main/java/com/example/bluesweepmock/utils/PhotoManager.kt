package com.example.bluesweepmock.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PhotoManager(private val context: Context) {
    
    // Create a temporary file for camera photos
    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }
    
    // Get content URI for the image file
    fun getUriForFile(file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "com.example.bluesweepmock.fileprovider",
            file
        )
    }
    
    // Save bitmap to a file and return its URI
    fun saveBitmapToFile(bitmap: Bitmap): Uri? {
        val filename = "waste_report_${System.currentTimeMillis()}.jpg"
        
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10 and above, use MediaStore
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/BlueSweep")
            }
            
            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ) ?: return null
            
            try {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                }
                uri
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        } else {
            // For older versions, save to app's private directory
            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                filename
            )
            
            try {
                FileOutputStream(file).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                }
                FileProvider.getUriForFile(
                    context,
                    "com.example.bluesweepmock.fileprovider",
                    file
                )
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }
    
    // Save a sample image for testing purposes
    fun getSampleImageUri(): Uri? {
        val sampleDrawableId = com.example.bluesweepmock.R.drawable.plastic_pollution
        val bitmap = android.graphics.BitmapFactory.decodeResource(context.resources, sampleDrawableId)
        return saveBitmapToFile(bitmap)
    }
} 