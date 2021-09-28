package com.simpliest.giveget.matsfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.simpliest.giveget.R

class Search : Fragment(R.layout.fragment_dashboard) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        SavedInstance: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

}