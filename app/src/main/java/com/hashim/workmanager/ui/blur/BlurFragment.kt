package com.hashim.workmanager.ui.blur

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.hashim.workmanager.Constants.Companion.IMAGE_URI
import com.hashim.workmanager.databinding.FragmentBlurBinding
import com.hashim.workmanager.ui.BlurViewModel
import com.hashim.workmanager.ui.blur.OutputEvents.OnSetImageUri
import timber.log.Timber

class BlurFragment : Fragment() {
    lateinit var hFragmentBlurBinding: FragmentBlurBinding
    val hBlurViewModel: BlurViewModel by activityViewModels()
    var hBlurLevel: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        hFragmentBlurBinding = FragmentBlurBinding.inflate(
            inflater,
            container,
            false
        )
        return hFragmentBlurBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hSubscribeObservers()
        hSetListeners()

    }

    private fun hSetListeners() {
        hFragmentBlurBinding.destinations.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                hFragmentBlurBinding.radioBlurLv1.id -> {
                    hBlurLevel = 1
                }
                hFragmentBlurBinding.radioBlurLv2.id -> {
                    hBlurLevel = 2
                }
                hFragmentBlurBinding.radioBlurLv3.id -> {
                    hBlurLevel = 3
                }
            }
        }
        hFragmentBlurBinding.goButton.setOnClickListener {
            hBlurViewModel.hApplyBlur(hBlurLevel)
        }
    }

    private fun hSubscribeObservers() {
        hBlurViewModel.hOutputEvents.observe(viewLifecycleOwner) {
            when (it) {
                is OnSetImageUri -> {
                    Glide.with(requireContext())
                        .load(it.uri)
                        .into(hFragmentBlurBinding.imageView)
                }
            }
        }
        hBlurViewModel.hWorkInfoLd.observe(viewLifecycleOwner) { listWorkInfo ->
            if (listWorkInfo.isNullOrEmpty()) {
                Timber.d("Null list")
            } else {
                Timber.d("Work info $listWorkInfo")
                val hWorkInfo = listWorkInfo.get(0)
                if (hWorkInfo.state.isFinished) {
                    hHideProgressBar()
                    Timber.d("Work is finished")
                    val hOutputImageUri = hWorkInfo.outputData.getString(IMAGE_URI)
                    if (!hOutputImageUri.isNullOrEmpty()) {
                        hBlurViewModel.hSetImageUri(Uri.parse(hOutputImageUri))
                    }
                } else {
                    hShowProgressBar()
                }
            }
        }
    }

    private fun hShowProgressBar() {
        with(hFragmentBlurBinding) {
            progressBar.visibility = View.VISIBLE
            cancelButton.visibility = View.VISIBLE
            goButton.visibility = View.GONE
            seeFileButton.visibility = View.GONE
        }

    }

    private fun hHideProgressBar() {
        with(hFragmentBlurBinding) {
            progressBar.visibility = View.GONE
            cancelButton.visibility = View.GONE
            goButton.visibility = View.VISIBLE
        }
    }
}
