package dev.dhruva.mycastnowrecuriter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.domain.usecase.SubmitDeliverableReviewUseCase
import dev.dhruva.mycastnowrecuriter.model.ReviewChecklistItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DeliverableReviewUiState(
    val campaignTitle: String = "Nike India · Aditi Sharma",
    val isPlaying: Boolean = false,
    val currentPositionSec: Int = 14,
    val totalDurationSec: Int = 24,
    val checklist: List<ReviewChecklistItem> = MockData.reviewChecklist,
    val isSubmitting: Boolean = false,
    val revisionNotes: String = "",
    val showRevisionModal: Boolean = false,
    val errorMessage: String? = null
) {
    val checkedCount: Int get() = checklist.count { it.isChecked }
    val allChecked: Boolean get() = checkedCount == checklist.size
}

sealed interface DeliverableReviewEvent {
    data object NavigateToMilestoneRelease : DeliverableReviewEvent
    data class ShowMessage(val message: String) : DeliverableReviewEvent
}

class DeliverableReviewViewModel(
    private val submitReviewUseCase: SubmitDeliverableReviewUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeliverableReviewUiState())
    val uiState: StateFlow<DeliverableReviewUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<DeliverableReviewEvent>()
    val events: SharedFlow<DeliverableReviewEvent> = _events.asSharedFlow()

    fun init(campaignTitle: String) {
        _uiState.update { it.copy(campaignTitle = campaignTitle) }
    }

    fun togglePlayPause() {
        _uiState.update { it.copy(isPlaying = !it.isPlaying) }
    }

    fun toggleChecklistItem(index: Int) {
        val list = _uiState.value.checklist.toMutableList()
        val item = list[index]
        list[index] = item.copy(isChecked = !item.isChecked)
        _uiState.update { it.copy(checklist = list) }
    }

    fun openRevisionModal() {
        _uiState.update { it.copy(showRevisionModal = true) }
    }

    fun closeRevisionModal() {
        _uiState.update { it.copy(showRevisionModal = false) }
    }

    fun updateRevisionNotes(notes: String) {
        _uiState.update { it.copy(revisionNotes = notes) }
    }

    fun submitRevisionRequest() {
        val notes = _uiState.value.revisionNotes
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, showRevisionModal = false) }
            val res = submitReviewUseCase("camp_1", isApproved = false, revisionNotes = notes)
            res.onSuccess {
                _uiState.update { it.copy(isSubmitting = false) }
                _events.emit(DeliverableReviewEvent.ShowMessage("Revision request submitted to creator"))
            }
        }
    }

    fun approveDeliverable() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            val res = submitReviewUseCase("camp_1", isApproved = true)
            res.onSuccess {
                _uiState.update { it.copy(isSubmitting = false) }
                _events.emit(DeliverableReviewEvent.NavigateToMilestoneRelease)
            }
        }
    }
}
