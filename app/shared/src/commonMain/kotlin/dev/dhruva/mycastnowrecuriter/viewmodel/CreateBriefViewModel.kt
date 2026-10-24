package dev.dhruva.mycastnowrecuriter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dhruva.mycastnowrecuriter.domain.usecase.CreateCampaignBriefUseCase
import dev.dhruva.mycastnowrecuriter.model.Campaign
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CreateBriefUiState(
    val brandName: String = "Northstar Beauty",
    val title: String = "",
    val objective: String = "Brand Awareness",
    val selectedCategory: String = "Fashion & Beauty",
    val selectedDeliverables: Set<String> = setOf("1× 4K Instagram Reel", "30-day Meta Ads Whitelisting"),
    val budgetPerCreator: Long = 25000L,
    val creatorSlots: Int = 2,
    val turnaroundDays: Int = 3,
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
) {
    val subtotal: Long get() = budgetPerCreator * creatorSlots
    val gst: Long get() = (subtotal * 18) / 100
    val totalEscrow: Long get() = subtotal + gst
    val formattedTotal: String get() = "₹${totalEscrow}"
    val isValid: Boolean get() = title.isNotBlank() && selectedDeliverables.isNotEmpty()
}

sealed interface CreateBriefEvent {
    data class BriefCreatedSuccess(val campaign: Campaign) : CreateBriefEvent
    data class ShowMessage(val message: String) : CreateBriefEvent
}

class CreateBriefViewModel(
    private val createBriefUseCase: CreateCampaignBriefUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateBriefUiState())
    val uiState: StateFlow<CreateBriefUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CreateBriefEvent>()
    val events: SharedFlow<CreateBriefEvent> = _events.asSharedFlow()

    fun onTitleChanged(newTitle: String) {
        _uiState.update { it.copy(title = newTitle, errorMessage = null) }
    }

    fun onCategorySelected(cat: String) {
        _uiState.update { it.copy(selectedCategory = cat) }
    }

    fun onObjectiveSelected(obj: String) {
        _uiState.update { it.copy(objective = obj) }
    }

    fun toggleDeliverable(deliverable: String) {
        val current = _uiState.value.selectedDeliverables.toMutableSet()
        if (current.contains(deliverable)) {
            if (current.size > 1) current.remove(deliverable)
        } else {
            current.add(deliverable)
        }
        _uiState.update { it.copy(selectedDeliverables = current) }
    }

    fun onBudgetPerCreatorChanged(amount: Long) {
        _uiState.update { it.copy(budgetPerCreator = amount.coerceIn(5000L, 500000L)) }
    }

    fun onCreatorSlotsChanged(slots: Int) {
        _uiState.update { it.copy(creatorSlots = slots.coerceIn(1, 10)) }
    }

    fun onTurnaroundDaysChanged(days: Int) {
        _uiState.update { it.copy(turnaroundDays = days.coerceIn(1, 14)) }
    }

    fun submitBrief() {
        val state = _uiState.value
        if (!state.isValid) {
            _uiState.update { it.copy(errorMessage = "Please specify a campaign title and at least one deliverable.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            val res = createBriefUseCase(
                title = state.title,
                category = state.selectedCategory,
                budget = state.totalEscrow
            )
            res.onSuccess { campaign ->
                _uiState.update { it.copy(isSubmitting = false, isSuccess = true) }
                _events.emit(CreateBriefEvent.BriefCreatedSuccess(campaign))
            }
            res.onError { err ->
                _uiState.update { it.copy(isSubmitting = false, errorMessage = err.message) }
            }
        }
    }
}
