package com.example.android_tv_frontend.databinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.android_tv_frontend.R

// Lightweight manual binding to avoid enabling data binding feature beyond viewBinding
class FragmentDetailsBinding private constructor(
    val root: View,
    val detailsPoster: ImageView,
    val detailsTitle: TextView,
    val detailsSynopsis: TextView,
    val rateButton: Button
) {
    companion object {
        fun inflate(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean = false): FragmentDetailsBinding {
            val view = inflater.inflate(R.layout.fragment_details, container, attachToRoot)
            return bind(view)
        }

        fun bind(view: View): FragmentDetailsBinding {
            return FragmentDetailsBinding(
                root = view,
                detailsPoster = view.findViewById(R.id.details_poster),
                detailsTitle = view.findViewById(R.id.details_title),
                detailsSynopsis = view.findViewById(R.id.details_synopsis),
                rateButton = view.findViewById(R.id.rate_button)
            )
        }
    }
}
