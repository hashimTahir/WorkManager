package com.hashim.workmanager

class Constants {
    companion object {
        const val hTag = "hashimTimberTags %s"
        const val TAG_OUTPUT = "OUTPUT"
        const val IMAGE_WORK = "hImageWork"
        const val IMAGE_URI = "hImageUri"
        const val OUTPUT_PATH = "blur_filter_outputs"


        @JvmField
        val NOTIFICATION_CHANNEL_NAME: CharSequence = "WorkManager Notifications"

        const val NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever work starts"

        @JvmField
        val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"
        const val CHANNEL_ID = "NOTIFICATION"
        const val NOTIFICATION_ID = 1

        const val DELAY_TIME_MILLIS: Long = 3000
    }
}