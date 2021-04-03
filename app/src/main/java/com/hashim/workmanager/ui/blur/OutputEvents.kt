package com.hashim.workmanager.ui.blur

import android.net.Uri

sealed class OutputEvents {
    data class OnSetImageUri(val uri: Uri) : OutputEvents()
}