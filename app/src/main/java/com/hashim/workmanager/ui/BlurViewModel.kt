package com.hashim.workmanager.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.Data
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.hashim.workmanager.Constants
import com.hashim.workmanager.Constants.Companion.IMAGE_WORK
import com.hashim.workmanager.Constants.Companion.TAG_OUTPUT
import com.hashim.workmanager.ui.blur.OutputEvents
import com.hashim.workmanager.ui.blur.OutputEvents.OnSetImageUri

class BlurViewModel(application: Application) : AndroidViewModel(application) {

    private var hImageUri: Uri? = null
    private val hWorkManager = WorkManager.getInstance(application)
    private val hWorkInfoLd: LiveData<List<WorkInfo>>

    private val _hOutputEvents = MutableLiveData<OutputEvents>()
    val hOutputEvents: LiveData<OutputEvents>
        get() = _hOutputEvents

    init {
        hWorkInfoLd = hWorkManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
    }

    fun hCancelWork() {
        hWorkManager.cancelUniqueWork(IMAGE_WORK)
    }


    /*work.Data is used to pass the data around to/from the worker class
    * data allocated as key-value pairs*/
    fun hCreateDataForWork(): Data {
        return Data.Builder().apply {
            hImageUri?.let {
                putString(Constants.IMAGE_URI, it.toString())
            }
        }.build()
    }

    /*Apply the blur effect on the selected image to emulate the long running task*/
    fun hApplyBlur(hBlurLevel: Int) {
//
//        var hWork = hWorkManager.beginUniqueWork(
//            IMAGE_WORK,                //Work Name
//            ExistingWorkPolicy.REPLACE,   //Polices- Replace-Keep-Append-Append-Or-Replace  (self-explanatory)
//            OneTimeWorkRequest.from(/*Todo: add the worker*/)    //Request type--OneTimeWorkRequest  -PeriodicWorkRequest
//        )

    }

    fun hSetImageUri(uri: Uri?) {
        uri?.let {
            hImageUri = uri
            _hOutputEvents.value = OnSetImageUri(hImageUri!!)
        }
    }
}