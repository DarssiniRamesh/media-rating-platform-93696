package com.example.android_tv_frontend.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_tv_frontend.data.api.MediaItem
import com.example.android_tv_frontend.domain.APARepository
import com.example.android_tv_frontend.domain.ContentRepository
import com.example.android_tv_frontend.domain.RatingsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class ActionType { LIKE, LOVE, DISLIKE, CLOSE }

data class RatingUiState(
    val visible: Boolean = false,
    val title: String = "",
    val posterUrl: String = "",
    val message: String = "",
    val remainingSeconds: Int = 30,
    val focusedAction: ActionType = ActionType.CLOSE,
    val isRated: Boolean = false,
    val missingKeys: List<String> = emptyList(),
    val emptyKeys: List<String> = emptyList()
)

class RatingViewModel(
    private val ratingsRepo: RatingsRepository,
    private val contentRepo: ContentRepository,
    private val apaRepo: APARepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RatingUiState())
    val uiState: StateFlow<RatingUiState> = _uiState

    private var countdownJob: Job? = null
    private var currentItem: MediaItem? = null

    // PUBLIC_INTERFACE
    fun prepareFor(groupId: Long) {
        viewModelScope.launch {
            val prior = ratingsRepo.getPriorLike(groupId)
            if (prior != 0) {
                // Already rated; do not show overlay
                _uiState.value = _uiState.value.copy(visible = false, isRated = true)
                return@launch
            }
            val item = contentRepo.getItem(groupId)
            currentItem = item
            val settings = apaRepo.getSettings()
            val legends = apaRepo.getLegendKeys()

            val titleKey = legends["rating_title_key"] ?: "rating_title_key"
            val messageKey = legends["rating_message_key"] ?: "rating_message_key"

            val missing = mutableListOf<String>()
            if (!legends.containsKey("rating_title_key")) missing.add("rating_title_key")
            if (!legends.containsKey("rating_message_key")) missing.add("rating_message_key")

            _uiState.value = RatingUiState(
                visible = true,
                title = item.title,
                posterUrl = item.posterUrl,
                message = if (messageKey.isEmpty()) "" else "Your vote helps us recommend more content like this.",
                remainingSeconds = settings.displayTime,
                focusedAction = ActionType.CLOSE,
                isRated = false,
                missingKeys = missing,
                emptyKeys = legends.filterValues { it.isEmpty() }.keys.toList()
            )
            startCountdown(settings.displayTime)
        }
    }

    private fun startCountdown(secs: Int) {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            var left = secs
            while (left > 0) {
                _uiState.value = _uiState.value.copy(remainingSeconds = left)
                delay(1000)
                left--
            }
            _uiState.value = _uiState.value.copy(visible = false)
        }
    }

    // PUBLIC_INTERFACE
    fun onActionSelected(action: ActionType) {
        val item = currentItem ?: return
        when (action) {
            ActionType.CLOSE -> _uiState.value = _uiState.value.copy(visible = false)
            ActionType.LIKE -> submit(item.groupId, 1)
            ActionType.LOVE -> submit(item.groupId, 2)
            ActionType.DISLIKE -> submit(item.groupId, -1)
        }
    }

    private fun submit(groupId: Long, like: Int) {
        viewModelScope.launch {
            ratingsRepo.submitLike(groupId, like)
            _uiState.value = _uiState.value.copy(visible = false, isRated = true)
        }
    }
}
