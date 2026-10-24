package dev.dhruva.mycastnowrecuriter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.domain.usecase.ToggleBookmarkCreatorUseCase
import dev.dhruva.mycastnowrecuriter.model.Creator
import dev.dhruva.mycastnowrecuriter.model.DeliverableItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TalentProfileUiState(
    val creator: Creator? = null,
    val deliverables: List<DeliverableItem> = MockData.defaultDeliverables,
    val totalAmount: Long = 0,
    val selectedCount: Int = 0,
    val isBookmarked: Boolean = false,
    val isLoading: Boolean = false
) {
    val selectedDeliverables: List<DeliverableItem> get() = deliverables.filter { it.isSelected }

    val summaryText: String
        get() = when (selectedCount) {
            0 -> "No deliverables selected"
            1 -> "1 deliverable selected"
            2 -> "Includes Reel + 30-day ads"
            else -> "$selectedCount deliverables selected"
        }
}

sealed interface TalentProfileEvent {
    data class ProceedToBooking(val creator: Creator, val selectedDeliverables: List<DeliverableItem>) : TalentProfileEvent
    data class ShowMessage(val message: String) : TalentProfileEvent
}

class TalentProfileViewModel(
    private val toggleBookmarkUseCase: ToggleBookmarkCreatorUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TalentProfileUiState())
    val uiState: StateFlow<TalentProfileUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<TalentProfileEvent>()
    val events: SharedFlow<TalentProfileEvent> = _events.asSharedFlow()

    fun init(creator: Creator, initialDeliverables: List<DeliverableItem>? = null) {
        val items = initialDeliverables ?: MockData.defaultDeliverables
        val sum = items.filter { it.isSelected }.sumOf { it.priceAmount }
        val count = items.count { it.isSelected }

        _uiState.update {
            it.copy(
                creator = creator,
                deliverables = items,
                totalAmount = sum,
                selectedCount = count
            )
        }
        checkBookmarkStatus(creator.id)
    }

    fun onToggleDeliverable(index: Int) {
        val list = _uiState.value.deliverables.toMutableList()
        val item = list[index]
        list[index] = item.copy(isSelected = !item.isSelected)

        val newSum = list.filter { it.isSelected }.sumOf { it.priceAmount }
        val newCount = list.count { it.isSelected }

        _uiState.update {
            it.copy(
                deliverables = list,
                totalAmount = newSum,
                selectedCount = newCount
            )
        }
    }

    fun onToggleBookmark() {
        val creator = _uiState.value.creator ?: return
        viewModelScope.launch {
            toggleBookmarkUseCase(creator.id).collect { res ->
                res.onSuccess { isSaved ->
                    _uiState.update { it.copy(isBookmarked = isSaved) }
                    val msg = if (isSaved) "Saved ${creator.name} to shortlist" else "Removed from shortlist"
                    _events.emit(TalentProfileEvent.ShowMessage(msg))
                }
            }
        }
    }

    fun onBookClicked() {
        val creator = _uiState.value.creator ?: return
        val selected = _uiState.value.selectedDeliverables
        if (selected.isEmpty()) {
            viewModelScope.launch {
                _events.emit(TalentProfileEvent.ShowMessage("Please select at least one deliverable"))
            }
            return
        }
        viewModelScope.launch {
            _events.emit(TalentProfileEvent.ProceedToBooking(creator, selected))
        }
    }

    private fun checkBookmarkStatus(creatorId: String) {
        viewModelScope.launch {
            toggleBookmarkUseCase.observeSavedIds().collect { ids ->
                _uiState.update { it.copy(isBookmarked = ids.contains(creatorId)) }
            }
        }
    }
}
