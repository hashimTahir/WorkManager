package com.hashim.workmanager.ui.events

import android.net.Uri

sealed class OutputEvents {
    data class OnSetImageUri(val uri: Uri) : OutputEvents()
}