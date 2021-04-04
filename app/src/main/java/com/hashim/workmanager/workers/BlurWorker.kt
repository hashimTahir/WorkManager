package com.hashim.workmanager.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.hashim.workmanager.utis.Constants.Companion.IMAGE_URI
import com.hashim.workmanager.utis.hBlurBitmap
import com.hashim.workmanager.utis.hMakeNotification
import com.hashim.workmanager.utis.hSleep
import com.hashim.workmanager.utis.hWriteBitmapToFile
import timber.log.Timber


class BlurWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        Timber.d("BlurWorker Called")

        val hContext = applicationContext

        val hUri = inputData.getString(IMAGE_URI)

        hMakeNotification("Blurring image", hContext)
        hSleep()

        return try {
            if (TextUtils.isEmpty(hUri)) {
                Timber.d("Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val hResolver = hContext.contentResolver

            val hPicture = BitmapFactory.decodeStream(
                hResolver.openInputStream(Uri.parse(hUri))
            )

            val output = hBlurBitmap(hPicture, hContext)

            // Write bitmap to a temp file
            val hOutputUri = hWriteBitmapToFile(hContext, output)

            val hOutputData = workDataOf(IMAGE_URI to hOutputUri.toString())

            Result.success(hOutputData)
        } catch (throwable: Throwable) {
            Timber.d(throwable, "Error applying blur")
            Result.failure()
        }
    }
}