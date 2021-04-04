package com.hashim.workmanager.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.hashim.workmanager.utis.Constants.Companion.IMAGE_URI
import com.hashim.workmanager.utis.hMakeNotification
import com.hashim.workmanager.utis.hSleep
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class SaveImageWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(
    context,
    workerParams
) {
    private val Title = "Blurred Image"
    private val hDateFormatter = SimpleDateFormat(
        "yyyy.MM.dd 'at' HH:mm:ss z",
        Locale.getDefault()
    )

    override fun doWork(): Result {
        Timber.d("Save Work Called")

        hMakeNotification("Saving image", applicationContext)
        hSleep()

        val resolver = applicationContext.contentResolver
        return try {
            val hResourseUri = inputData.getString(IMAGE_URI)
            val hBitmap = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(hResourseUri))
            )
            val hImageUrl = MediaStore.Images.Media.insertImage(
                resolver, hBitmap, Title, hDateFormatter.format(Date())
            )
            if (!hImageUrl.isNullOrEmpty()) {
                val output = workDataOf(IMAGE_URI to hImageUrl)

                Result.success(output)
            } else {
                Timber.e("Writing to MediaStore failed")
                Result.failure()
            }
        } catch (exception: Exception) {
            Timber.e(exception)
            Result.failure()
        }
    }
}
