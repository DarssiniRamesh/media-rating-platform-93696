package com.example.android_tv_frontend.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.android_tv_frontend.R
import com.example.android_tv_frontend.data.api.ApiClient
import com.example.android_tv_frontend.databinding.FragmentDetailsBinding
import com.example.android_tv_frontend.domain.APARepository
import com.example.android_tv_frontend.domain.ContentRepository
import com.example.android_tv_frontend.domain.RatingsRepository
import com.example.android_tv_frontend.ui.RatingViewModel
import com.example.android_tv_frontend.ui.rating.RatingDialogFragment
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val groupId = arguments?.getLong("groupId") ?: 1L
        binding.detailsTitle.text = "Movie $groupId"
        binding.detailsSynopsis.text = getString(R.string.details_synopsis_placeholder)

        Glide.with(requireContext())
            .load("https://picsum.photos/seed/${groupId}/1200/675")
            .centerCrop()
            .into(binding.detailsPoster)

        binding.rateButton.setOnClickListener {
            RatingDialogFragment.newInstance(groupId)
                .show(parentFragmentManager, "rate")
        }

        viewLifecycleOwner.lifecycleScope.launch {
            ratingVm.prepareFor(groupId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
