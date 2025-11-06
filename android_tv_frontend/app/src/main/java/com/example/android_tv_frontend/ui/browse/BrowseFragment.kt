package com.example.android_tv_frontend.ui.browse

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.Presenter
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.android_tv_frontend.R
import com.example.android_tv_frontend.data.api.MediaItem
import com.example.android_tv_frontend.data.api.ApiClient
import com.example.android_tv_frontend.domain.APARepository
import com.example.android_tv_frontend.domain.ContentRepository
import com.example.android_tv_frontend.domain.RatingsRepository
import com.example.android_tv_frontend.ui.RatingViewModel

class BrowseFragment : BrowseSupportFragment() {

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

    private val rowsAdapter: ArrayObjectAdapter by lazy {
        ArrayObjectAdapter(ListRowPresenter())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUi()
        loadRows()
        setOnItemViewClickedListener { _, item, _, _ ->
            if (item is MediaItem) {
                val args = bundleOf("groupId" to item.groupId)
                findNavController().navigate(R.id.action_browse_to_details, args)
            }
        }
    }

    private fun setupUi() {
        title = getString(R.string.app_name)
        brandColor = resources.getColor(R.color.ocean_primary)
        headersState = HEADERS_DISABLED
        isHeadersTransitionOnBackEnabled = false
        searchAffordanceColor = Color.WHITE
        adapter = rowsAdapter
    }

    private fun loadRows() {
        val cardPresenter = MediaCardPresenter()
        val listRowAdapter = ArrayObjectAdapter(cardPresenter)
        // Placeholder data
        for (i in 1..20) {
            listRowAdapter.add(
                MediaItem(
                    groupId = i.toLong(),
                    title = "Movie $i",
                    posterUrl = "https://picsum.photos/seed/$i/600/338",
                    backgroundUrl = "https://picsum.photos/seed/${i}b/1200/675"
                )
            )
        }
        rowsAdapter.add(ListRow(HeaderItem(0, "Featured"), listRowAdapter))
    }
}

class MediaCardPresenter : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val cardView = ImageCardView(parent.context).apply {
            isFocusable = true
            isFocusableInTouchMode = true
            setMainImageDimensions(320, 180)
            setBackgroundColor(Color.TRANSPARENT)
            setInfoAreaBackgroundColor(Color.parseColor("#AAFFFFFF"))
        }
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val media = item as MediaItem
        val cardView = viewHolder.view as ImageCardView
        cardView.titleText = media.title
        // Aggregate rating would come from repo; show placeholder stars text overlay
        cardView.contentText = "★ ${String.format("%.1f", media.avgRating)} (${media.ratingsCount})"
        Glide.with(cardView.context)
            .load(media.posterUrl)
            .centerCrop()
            .into(cardView.mainImageView)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        cardView.mainImage = null
    }
}
