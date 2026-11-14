package dev.dhruva.mycastnowrecuriter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.domain.usecase.GetCompanyProfileUseCase
import dev.dhruva.mycastnowrecuriter.model.CompanyProfile
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CompanyUiState(
    val profile: CompanyProfile = MockData.companyProfile,
    val activeSettingModal: String? = null,
    val isEscrowInfoModalOpen: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface CompanyEvent {
    data class ShowMessage(val message: String) : CompanyEvent
}

class CompanyViewModel(
    private val getCompanyProfileUseCase: GetCompanyProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompanyUiState())
    val uiState: StateFlow<CompanyUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CompanyEvent>()
    val events: SharedFlow<CompanyEvent> = _events.asSharedFlow()

    init {
        loadProfile()
    }

    fun onSettingClicked(settingTitle: String) {
        _uiState.update { it.copy(activeSettingModal = settingTitle) }
    }

    fun onCloseSettingModal() {
        _uiState.update { it.copy(activeSettingModal = null) }
    }

    fun onEscrowLearnMoreClicked() {
        _uiState.update { it.copy(isEscrowInfoModalOpen = true) }
    }

    fun onCloseEscrowInfoModal() {
        _uiState.update { it.copy(isEscrowInfoModalOpen = false) }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            getCompanyProfileUseCase().collect { res ->
                res.onLoading { _uiState.update { it.copy(isLoading = true) } }
                res.onSuccess { p -> _uiState.update { it.copy(isLoading = false, profile = p) } }
                res.onError { err -> _uiState.update { it.copy(isLoading = false, errorMessage = err.message) } }
            }
        }
    }
}
