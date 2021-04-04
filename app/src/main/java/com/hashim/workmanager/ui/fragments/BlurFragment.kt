package com.hashim.workmanager.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.hashim.workmanager.databinding.FragmentBlurBinding
import com.hashim.workmanager.ui.MainViewModel
import com.hashim.workmanager.ui.events.OutputEvents.OnSetImageUri
import com.hashim.workmanager.utis.Constants.Companion.IMAGE_URI
import timber.log.Timber

class BlurFragment : Fragment() {
    lateinit var hFragmentBlurBinding: FragmentBlurBinding
    val hMainViewModel: MainViewModel by activityViewModels()
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
            hMainViewModel.hApplyBlur(hBlurLevel)
        }
    }

    private fun hSubscribeObservers() {
        hMainViewModel.hOutputEvents.observe(viewLifecycleOwner) {
            when (it) {
                is OnSetImageUri -> {
                    Glide.with(requireContext())
                        .load(it.uri)
                        .into(hFragmentBlurBinding.imageView)
                }
            }
        }
        hMainViewModel.hWorkInfoLd.observe(viewLifecycleOwner) { listWorkInfo ->
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
                        hMainViewModel.hSetImageUri(Uri.parse(hOutputImageUri))
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
