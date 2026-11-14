package dev.dhruva.mycastnowrecuriter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.domain.usecase.LockEscrowDepositUseCase
import dev.dhruva.mycastnowrecuriter.model.Creator
import dev.dhruva.mycastnowrecuriter.model.DeliverableItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class BookingCheckoutUiState(
    val creator: Creator = MockData.aditiCreator,
    val deliverables: List<DeliverableItem> = emptyList(),
    val talentFee: Long = 0,
    val gstAmount: Long = 0,
    val totalDeposit: Long = 0,
    val isProcessing: Boolean = false,
    val transactionRef: String? = null,
    val errorMessage: String? = null
)

sealed interface BookingCheckoutEvent {
    data class EscrowLockedSuccess(val transactionId: String) : BookingCheckoutEvent
    data class ShowError(val message: String) : BookingCheckoutEvent
    data object NavigateBackToScope : BookingCheckoutEvent
}

class BookingCheckoutViewModel(
    private val lockEscrowDepositUseCase: LockEscrowDepositUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingCheckoutUiState())
    val uiState: StateFlow<BookingCheckoutUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<BookingCheckoutEvent>()
    val events: SharedFlow<BookingCheckoutEvent> = _events.asSharedFlow()

    fun init(creator: Creator, selectedItems: List<DeliverableItem>) {
        val items = if (selectedItems.isNotEmpty()) selectedItems else MockData.defaultDeliverables.take(2)
        val fee = items.sumOf { it.priceAmount }
        val gst = (fee * 18) / 100
        val total = fee + gst

        _uiState.update {
            it.copy(
                creator = creator,
                deliverables = items,
                talentFee = fee,
                gstAmount = gst,
                totalDeposit = total,
                isProcessing = false
            )
        }
    }

    fun onLockDepositClicked() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, errorMessage = null) }
            val res = lockEscrowDepositUseCase(
                campaignId = "camp_1",
                creatorId = state.creator.id,
                totalAmount = state.totalDeposit
            )
            res.onSuccess { txnId ->
                _uiState.update { it.copy(isProcessing = false, transactionRef = txnId) }
                _events.emit(BookingCheckoutEvent.EscrowLockedSuccess(txnId))
            }
            res.onError { err ->
                _uiState.update { it.copy(isProcessing = false, errorMessage = err.message) }
                _events.emit(BookingCheckoutEvent.ShowError(err.message))
            }
        }
    }

    fun onEditScopeClicked() {
        viewModelScope.launch {
            _events.emit(BookingCheckoutEvent.NavigateBackToScope)
        }
    }
}
