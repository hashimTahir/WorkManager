package com.hashim.workmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.hashim.workmanager.Constants.Companion.H_GET_IMAGE_CONTENT
import com.hashim.workmanager.PermissionsUtils.Companion.hRequestPermission
import com.hashim.workmanager.R
import com.hashim.workmanager.databinding.FragmentSelectImageBinding
import timber.log.Timber


class SelectImageFragment : Fragment() {
    private var hPermissionsCount: Int = 0
    lateinit var hFragmentSelectImageBinding: FragmentSelectImageBinding
    val hBlurViewModel: BlurViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hFragmentSelectImageBinding = FragmentSelectImageBinding.inflate(
            inflater,
            container,
            false
        )
        return hFragmentSelectImageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hSetupListeners()

    }

    private fun hSetupListeners() {
        hFragmentSelectImageBinding.selectImage.setOnClickListener {
            hRequestPermission(requireContext(), hRequestPermissionLauncher) {
                hSelectImageLauncher.launch(H_GET_IMAGE_CONTENT)
            }
        }
    }

    private val hRequestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        {
            Timber.d("Counte $hPermissionsCount")
            it.forEach { (key, value) ->
                if (value) hPermissionsCount++
            }
            if (hPermissionsCount == 2) {
                hSelectImageLauncher.launch(H_GET_IMAGE_CONTENT)
            }
        }

    private val hSelectImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent())
        {
            hBlurViewModel.hSetImageUri(it)
            findNavController().navigate(R.id.action_hSelectImageFragment_to_hBlurFragment)
        }

}