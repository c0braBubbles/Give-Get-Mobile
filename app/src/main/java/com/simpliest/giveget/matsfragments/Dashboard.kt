package com.simpliest.giveget.matsfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentContainer
import com.google.android.gms.maps.SupportMapFragment
import com.simpliest.giveget.R
import kotlinx.android.synthetic.*


//
class Dashboard : Fragment(R.layout.fragment_dashboard) {

    lateinit var mapFragment : SupportMapFragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        SavedInstance: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

}