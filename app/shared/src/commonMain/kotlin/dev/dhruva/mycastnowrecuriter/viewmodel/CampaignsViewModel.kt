package dev.dhruva.mycastnowrecuriter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dhruva.mycastnowrecuriter.domain.usecase.CreateCampaignBriefUseCase
import dev.dhruva.mycastnowrecuriter.domain.usecase.GetCampaignsUseCase
import dev.dhruva.mycastnowrecuriter.model.Campaign
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CampaignsUiState(
    val campaigns: List<Campaign> = emptyList(),
    val selectedFilter: String = "All 6",
    val filters: List<String> = listOf("All 6", "Active 3", "In review 2", "Drafts 1"),
    val totalEscrowProtected: String = "₹2,10,000",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface CampaignsEvent {
    data class NavigateToEscrowTracker(val campaign: Campaign) : CampaignsEvent
    data class NavigateToReview(val campaign: Campaign) : CampaignsEvent
    data class ShowMessage(val message: String) : CampaignsEvent
}

class CampaignsViewModel(
    private val getCampaignsUseCase: GetCampaignsUseCase,
    private val createBriefUseCase: CreateCampaignBriefUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CampaignsUiState(isLoading = true))
    val uiState: StateFlow<CampaignsUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CampaignsEvent>()
    val events: SharedFlow<CampaignsEvent> = _events.asSharedFlow()

    init {
        loadCampaigns()
    }

    fun onFilterSelected(filter: String) {
        _uiState.update { it.copy(selectedFilter = filter) }
        loadCampaigns()
    }

    fun onNewBriefClicked() {
        viewModelScope.launch {
            _events.emit(CampaignsEvent.ShowMessage("New brief builder: Explore creators to cast"))
        }
    }

    fun onCampaignActionClicked(campaign: Campaign) {
        viewModelScope.launch {
            when (campaign.status) {
                dev.dhruva.mycastnowrecuriter.model.CampaignStatus.MILESTONE_PENDING -> {
                    _events.emit(CampaignsEvent.NavigateToReview(campaign))
                }
                else -> {
                    _events.emit(CampaignsEvent.NavigateToEscrowTracker(campaign))
                }
            }
        }
    }

    fun refresh() {
        loadCampaigns()
    }

    private fun loadCampaigns() {
        val filter = _uiState.value.selectedFilter
        viewModelScope.launch {
            getCampaignsUseCase(filter).collect { res ->
                res.onLoading {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                }
                res.onSuccess { list ->
                    _uiState.update { it.copy(isLoading = false, campaigns = list, errorMessage = null) }
                }
                res.onError { err ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = err.message) }
                }
            }
        }
    }
}
