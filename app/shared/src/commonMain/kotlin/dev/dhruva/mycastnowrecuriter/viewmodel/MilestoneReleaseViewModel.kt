package dev.dhruva.mycastnowrecuriter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dhruva.mycastnowrecuriter.domain.usecase.ReleaseMilestoneUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MilestoneReleaseUiState(
    val milestoneAmount: Long = 24000,
    val payoutFormatted: String = "₹24,000.00",
    val creatorName: String = "Aditi Sharma",
    val isReleasing: Boolean = false,
    val payoutRef: String? = null,
    val errorMessage: String? = null
)

sealed interface MilestoneReleaseEvent {
    data object NavigateToWrapUp : MilestoneReleaseEvent
    data class ShowMessage(val message: String) : MilestoneReleaseEvent
}

class MilestoneReleaseViewModel(
    private val releaseMilestoneUseCase: ReleaseMilestoneUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MilestoneReleaseUiState())
    val uiState: StateFlow<MilestoneReleaseUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MilestoneReleaseEvent>()
    val events: SharedFlow<MilestoneReleaseEvent> = _events.asSharedFlow()

    fun onConfirmReleaseClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isReleasing = true, errorMessage = null) }
            val res = releaseMilestoneUseCase("camp_1", _uiState.value.milestoneAmount)
            res.onSuccess { ref ->
                _uiState.update { it.copy(isReleasing = false, payoutRef = ref) }
                _events.emit(MilestoneReleaseEvent.NavigateToWrapUp)
            }
            res.onError { err ->
                _uiState.update { it.copy(isReleasing = false, errorMessage = err.message) }
                _events.emit(MilestoneReleaseEvent.ShowMessage(err.message))
            }
        }
    }
}
