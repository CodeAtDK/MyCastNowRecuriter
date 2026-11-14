package dev.dhruva.mycastnowrecuriter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CampaignWrapUiState(
    val campaignTitle: String = "Nike India · Summer Creator Campaign",
    val creatorName: String = "Aditi Sharma",
    val totalPaidFormatted: String = "₹28,320.00",
    val invoiceNumber: String = "INV-2026-8320",
    val rating: Int = 5,
    val isInvoiceDownloaded: Boolean = false
) {
    val ratingLabel: String
        get() = when (rating) {
            5 -> "Exceptional execution & turnaround! ★★★★★"
            4 -> "Great collaboration & quality"
            3 -> "Good delivery"
            else -> "Needs improvement"
        }
}

sealed interface CampaignWrapEvent {
    data object NavigateToDiscover : CampaignWrapEvent
    data object NavigateToCampaigns : CampaignWrapEvent
    data class ShowMessage(val message: String) : CampaignWrapEvent
}

class CampaignWrapViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CampaignWrapUiState())
    val uiState: StateFlow<CampaignWrapUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CampaignWrapEvent>()
    val events: SharedFlow<CampaignWrapEvent> = _events.asSharedFlow()

    fun onRatingChanged(newRating: Int) {
        _uiState.update { it.copy(rating = newRating) }
    }

    fun onDownloadInvoiceClicked() {
        _uiState.update { it.copy(isInvoiceDownloaded = true) }
        viewModelScope.launch {
            _events.emit(CampaignWrapEvent.ShowMessage("GST Invoice downloaded (${_uiState.value.invoiceNumber}.pdf)"))
        }
    }

    fun onBookAgainClicked() {
        viewModelScope.launch {
            _events.emit(CampaignWrapEvent.NavigateToDiscover)
        }
    }

    fun onBackToCampaignsClicked() {
        viewModelScope.launch {
            _events.emit(CampaignWrapEvent.NavigateToCampaigns)
        }
    }
}
