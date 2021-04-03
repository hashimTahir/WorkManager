package com.hashim.workmanager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import timber.log.Timber
import java.util.*


private val MAX_NUMBER_REQUEST_PERMISSIONS = 2
private var hPermissionsCount: Int = 0
private val hPermissions = Arrays.asList(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

class PermissionsUtils {
    companion object {
        fun hRequestPermission(
            context: Context,
            launcher: ActivityResultLauncher<Array<String>>,
            permisionCallBack: () -> Unit,
        ) {
            Timber.d("Requesting permissions")

            if (!hHasPermissions(context)) {
                if (hPermissionsCount < MAX_NUMBER_REQUEST_PERMISSIONS) {
                    launcher.launch(hPermissions.toTypedArray())
                }
            } else {
                permisionCallBack()
            }
        }

        private fun hHasPermissions(context: Context): Boolean {
            var hasPermissions = true
            for (permission in hPermissions) {
                hasPermissions = hasPermissions and isPermissionGranted(context, permission)
            }
            return hasPermissions
        }

        private fun isPermissionGranted(context: Context, permission: String) =
            ContextCompat.checkSelfPermission(context, permission) ==
                    PackageManager.PERMISSION_GRANTED

    }
}