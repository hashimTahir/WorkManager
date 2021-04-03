package com.hashim.workmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.hashim.workmanager.PermissionsUtils.Companion.hRequestPermission
import com.hashim.workmanager.databinding.FragmentSelectImageBinding
import timber.log.Timber

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SelectImageFragment : Fragment() {
    private var hPermissionsCount: Int = 0
    lateinit var hFragmentSelectImageBinding: FragmentSelectImageBinding
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
                hSelectImageLauncher.launch("image/*")
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
                hSelectImageLauncher.launch("image/*")
            }
        }

    private val hSelectImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent())
        {
            Timber.d("it$it")
        }

}