package com.hashim.workmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hashim.workmanager.databinding.FragmentBlurBinding

class BlurFragment : Fragment() {
    lateinit var hFragmentBlurBinding: FragmentBlurBinding
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

    }
}