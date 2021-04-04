package com.hashim.workmanager.utis

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hashim.workmanager.Constants.Companion.CHANNEL_ID
import com.hashim.workmanager.Constants.Companion.DELAY_TIME_MILLIS
import com.hashim.workmanager.Constants.Companion.NOTIFICATION_CHANNEL_DESCRIPTION
import com.hashim.workmanager.Constants.Companion.NOTIFICATION_CHANNEL_NAME
import com.hashim.workmanager.Constants.Companion.NOTIFICATION_ID
import com.hashim.workmanager.Constants.Companion.NOTIFICATION_TITLE
import com.hashim.workmanager.Constants.Companion.OUTPUT_PATH
import com.hashim.workmanager.R
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

@WorkerThread
fun hBlurBitmap(
    bitmap: Bitmap,
    applicationContext: Context
): Bitmap {
    lateinit var hRenderScript: RenderScript
    try {
        val hOutputBitmap = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height,
            bitmap.config
        )

        hRenderScript = RenderScript.create(applicationContext, RenderScript.ContextType.DEBUG)
        val hInAllocation = Allocation.createFromBitmap(hRenderScript, bitmap)
        val hOutAllocation = Allocation.createTyped(hRenderScript, hInAllocation.type)
        val hScriptIntrinsicBlur = ScriptIntrinsicBlur
            .create(
                hRenderScript,
                Element.U8_4(hRenderScript)
            )
        hScriptIntrinsicBlur.apply {
            setRadius(10f)
            hScriptIntrinsicBlur.setInput(hInAllocation)
            hScriptIntrinsicBlur.forEach(hOutAllocation)
        }
        hOutAllocation.copyTo(hOutputBitmap)

        return hOutputBitmap
    } finally {
        hRenderScript.finish()
    }
}


@Throws(FileNotFoundException::class)
fun hWriteBitmapToFile(
    applicationContext: Context,
    bitmap: Bitmap
):
        Uri {
    val hName = String.format("blur-filter-output-%s.png", UUID.randomUUID().toString())
    val hOutputDir = File(applicationContext.filesDir, OUTPUT_PATH)
    if (!hOutputDir.exists()) {
        hOutputDir.mkdirs()
    }
    val hOutputFile = File(hOutputDir, hName)
    var hFileOutputStream: FileOutputStream? = null
    try {
        hFileOutputStream = FileOutputStream(hOutputFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, hFileOutputStream)
    } finally {
        hFileOutputStream?.let {
            try {
                it.close()
            } catch (ignore: IOException) {
                Timber.d("Exception $ignore")
            }
        }
    }
    return Uri.fromFile(hOutputFile)
}


fun hMakeNotification(message: String, context: Context) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val hName = NOTIFICATION_CHANNEL_NAME
        val hDescription = NOTIFICATION_CHANNEL_DESCRIPTION
        val hImportance = NotificationManager.IMPORTANCE_HIGH
        val hChannel = NotificationChannel(CHANNEL_ID, hName, hImportance)
        hChannel.description = hDescription

        val hNotificationManger =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        hNotificationManger?.createNotificationChannel(hChannel)
    }

    val hBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))

    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, hBuilder.build())
}

/**
 * Method for sleeping for a fixed about of time to emulate slower work
 */
fun hSleep() {
    try {
        Thread.sleep(DELAY_TIME_MILLIS, 0)
    } catch (e: InterruptedException) {
        Timber.e(e.message)
    }

}