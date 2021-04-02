package com.hashim.workmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hashim.workmanager.databinding.FragmentSelectImageBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SelectImageFragment : Fragment() {

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

    }
}