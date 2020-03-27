package com.rockspin.flux.ui.success.fragment1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding3.view.clicks
import com.puddlealley.splash.android.events
import com.puddlealley.splash.android.actions
import com.rockspin.flux.R
import kotlinx.android.synthetic.main.fragment_sensors.*
import org.koin.android.viewmodel.ext.android.viewModel

class Fragment1 : Fragment() {

    private val fluxViewModel: Fragment1ViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_sensors, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fluxViewModel.events(this) {
            leftTrackerButton.clicks().map { Fragment1Events.ClickedOpen }
        }

        fluxViewModel.actions(this) {
            when (it) {
                is Fragment1Events.ClickedOpen-> findNavController().navigate(R.id.action_fragment1_to_fragment2)
            }
        }
    }
}


