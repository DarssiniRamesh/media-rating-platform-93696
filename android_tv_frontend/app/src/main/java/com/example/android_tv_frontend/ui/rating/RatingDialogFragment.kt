package com.example.android_tv_frontend.ui.rating

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.android_tv_frontend.R
import com.example.android_tv_frontend.data.api.ApiClient
import com.example.android_tv_frontend.domain.APARepository
import com.example.android_tv_frontend.domain.ContentRepository
import com.example.android_tv_frontend.domain.RatingsRepository
import com.example.android_tv_frontend.ui.ActionType
import com.example.android_tv_frontend.ui.RatingViewModel

class RatingDialogFragment : DialogFragment() {

    private val ratingVm: RatingViewModel by viewModels(factoryProducer = {
        val likes = ApiClient.likesService()
        val content = ApiClient.contentService()
        val apa = ApiClient.apaService()
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return RatingViewModel(
                    RatingsRepository(likes),
                    ContentRepository(content),
                    APARepository(apa)
                ) as T
            }
        }
    })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_rating, null, false)
        dialog.setContentView(view)

        val title: TextView = view.findViewById(R.id.rating_title)
        val message: TextView = view.findViewById(R.id.rating_message)
        val like: Button = view.findViewById(R.id.btn_like)
        val love: Button = view.findViewById(R.id.btn_love)
        val dislike: Button = view.findViewById(R.id.btn_dislike)
        val close: Button = view.findViewById(R.id.btn_close)

        title.text = getString(R.string.rating_title)
        message.text = getString(R.string.rating_message)

        like.setOnClickListener { ratingVm.onActionSelected(ActionType.LIKE); dismiss() }
        love.setOnClickListener { ratingVm.onActionSelected(ActionType.LOVE); dismiss() }
        dislike.setOnClickListener { ratingVm.onActionSelected(ActionType.DISLIKE); dismiss() }
        close.setOnClickListener { ratingVm.onActionSelected(ActionType.CLOSE); dismiss() }

        close.isFocusable = true
        close.requestFocus()

        dialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dismiss()
                true
            } else false
        }
        return dialog
    }

    companion object {
        fun newInstance(groupId: Long): RatingDialogFragment {
            val frag = RatingDialogFragment()
            val bundle = Bundle().apply { putLong("groupId", groupId) }
            frag.arguments = bundle
            return frag
        }
    }
}
