package dev.dhruva.mycastnowrecuriter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.domain.usecase.GetEscrowTimelineUseCase
import dev.dhruva.mycastnowrecuriter.domain.usecase.RequestPrioritySupportUseCase
import dev.dhruva.mycastnowrecuriter.model.EscrowTimelineStep
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class EscrowTrackerUiState(
    val campaignTitle: String = "Nike India · Summer Creator Campaign",
    val escrowAmountFormatted: String = "₹28,320.00",
    val creatorName: String = "Aditi Sharma",
    val deliverablesText: String = "Reel + 30-day ads",
    val steps: List<EscrowTimelineStep> = MockData.escrowSteps,
    val isLoading: Boolean = false,
    val supportTicketId: String? = null,
    val errorMessage: String? = null
)

sealed interface EscrowTrackerEvent {
    data object NavigateToDeliverableReview : EscrowTrackerEvent
    data class ShowMessage(val message: String) : EscrowTrackerEvent
}

class EscrowTrackerViewModel(
    private val getEscrowTimelineUseCase: GetEscrowTimelineUseCase,
    private val requestPrioritySupportUseCase: RequestPrioritySupportUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EscrowTrackerUiState())
    val uiState: StateFlow<EscrowTrackerUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<EscrowTrackerEvent>()
    val events: SharedFlow<EscrowTrackerEvent> = _events.asSharedFlow()

    fun init(campaignTitle: String, campaignId: String = "camp_1") {
        _uiState.update { it.copy(campaignTitle = campaignTitle) }
        loadSteps(campaignId)
    }

    fun onOpenReviewClicked() {
        viewModelScope.launch {
            _events.emit(EscrowTrackerEvent.NavigateToDeliverableReview)
        }
    }

    fun onRequestSupportClicked() {
        viewModelScope.launch {
            val res = requestPrioritySupportUseCase("camp_1")
            res.onSuccess { ticketId ->
                _uiState.update { it.copy(supportTicketId = ticketId) }
                _events.emit(EscrowTrackerEvent.ShowMessage("Priority escrow support ticket #$ticketId opened"))
            }
        }
    }

    private fun loadSteps(campaignId: String) {
        viewModelScope.launch {
            getEscrowTimelineUseCase(campaignId).collect { res ->
                res.onLoading { _uiState.update { it.copy(isLoading = true) } }
                res.onSuccess { steps -> _uiState.update { it.copy(isLoading = false, steps = steps) } }
                res.onError { err -> _uiState.update { it.copy(isLoading = false, errorMessage = err.message) } }
            }
        }
    }
}
