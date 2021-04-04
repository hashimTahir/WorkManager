package com.hashim.workmanager.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.hashim.workmanager.Constants.Companion.OUTPUT_PATH
import com.hashim.workmanager.utis.hMakeNotification
import com.hashim.workmanager.utis.hSleep
import timber.log.Timber
import java.io.File

class CleanupWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(
    context,
    workerParams
) {
    override fun doWork(): Result {
        // Makes a notification when the work starts and slows down the work so that
        hMakeNotification("Cleaning up old temporary files", applicationContext)
        hSleep()

        return try {
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if (entries != null) {
                    for (entry in entries) {
                        val name = entry.name
                        if (name.isNotEmpty() && name.endsWith(".png")) {
                            val deleted = entry.delete()
                            Timber.i("Deleted $name - $deleted")
                        }
                    }
                }
            }
            Result.success()
        } catch (exception: Exception) {
            Timber.e(exception)
            Result.failure()
        }
    }
}
