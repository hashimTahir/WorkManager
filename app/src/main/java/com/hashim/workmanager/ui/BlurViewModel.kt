package com.hashim.workmanager.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import com.hashim.workmanager.Constants
import com.hashim.workmanager.Constants.Companion.IMAGE_WORK
import com.hashim.workmanager.Constants.Companion.TAG_OUTPUT
import com.hashim.workmanager.ui.blur.OutputEvents
import com.hashim.workmanager.ui.blur.OutputEvents.OnSetImageUri
import com.hashim.workmanager.workers.BlurWorker
import com.hashim.workmanager.workers.CleanupWorker
import com.hashim.workmanager.workers.SaveImageWorker

class BlurViewModel(application: Application) : AndroidViewModel(application) {

    private var hImageUri: Uri? = null
    private val hWorkManager = WorkManager.getInstance(application)
    val hWorkInfoLd: LiveData<List<WorkInfo>>

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

        /*Clean up task if button is pressed multiple times. Clean up worker is used to
        * delete the saved files*/

        var hWork = hWorkManager.beginUniqueWork(
            IMAGE_WORK,                //Work Name
            ExistingWorkPolicy.REPLACE,   //Polices- Replace-Keep-Append-
            // Append-Or-Replace  (self-explanatory)
            OneTimeWorkRequest.from(CleanupWorker::class.java)    //Request type
            // --OneTimeWorkRequest  -PeriodicWorkRequest
        )

        for (level in 0 until hBlurLevel) {
            val hBlurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()
            /*The first time the apply blue is called  image uri would be the hImageUri
            * After the first blur the input data would be the output of previous operation*/

            if (level == 0) {
                /*Create data using the hImageUri passed from the view for the first time*/
                hBlurBuilder.setInputData(hCreateDataForWork())
            }

            /*when not 0 aplly blur worker will already have output data due to the first
            * operation*/
            /*Chaining works*/
            hWork = hWork.then(hBlurBuilder.build())

        }
        /*Constraints for the work*/
        val hWorkConstraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()


        val hSaveWork = OneTimeWorkRequestBuilder<SaveImageWorker>()
            .addTag(TAG_OUTPUT)
            .build()

        /*Chaining works togather*/
        hWork = hWork.then(hSaveWork)

        hWork.enqueue()

    }

    fun hSetImageUri(uri: Uri?) {
        uri?.let {
            hImageUri = uri
            _hOutputEvents.value = OnSetImageUri(hImageUri!!)
        }
    }
}