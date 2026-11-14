package dev.dhruva.mycastnowrecuriter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dhruva.mycastnowrecuriter.data.MockData
import dev.dhruva.mycastnowrecuriter.domain.usecase.GetUserProfileUseCase
import dev.dhruva.mycastnowrecuriter.domain.usecase.SwitchWorkspaceUseCase
import dev.dhruva.mycastnowrecuriter.domain.usecase.ToggleTwoFactorUseCase
import dev.dhruva.mycastnowrecuriter.model.UserProfile
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProfileUiState(
    val profile: UserProfile = MockData.userProfile,
    val isLoading: Boolean = false,
    val isSwitchingWorkspace: Boolean = false,
    val showWorkspaceModal: Boolean = false,
    val showLogoutConfirmDialog: Boolean = false,
    val errorMessage: String? = null
)

sealed interface ProfileEvent {
    data class ShowMessage(val message: String) : ProfileEvent
    data object SignedOut : ProfileEvent
}

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val switchWorkspaceUseCase: SwitchWorkspaceUseCase,
    private val toggleTwoFactorUseCase: ToggleTwoFactorUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ProfileEvent>()
    val events: SharedFlow<ProfileEvent> = _events.asSharedFlow()

    init {
        loadProfile()
    }

    fun openWorkspaceModal() {
        _uiState.update { it.copy(showWorkspaceModal = true) }
    }

    fun closeWorkspaceModal() {
        _uiState.update { it.copy(showWorkspaceModal = false) }
    }

    fun switchWorkspace(workspaceName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSwitchingWorkspace = true, showWorkspaceModal = false) }
            val res = switchWorkspaceUseCase(workspaceName)
            res.onSuccess { updated ->
                _uiState.update { it.copy(profile = updated, isSwitchingWorkspace = false) }
                _events.emit(ProfileEvent.ShowMessage("Switched to $workspaceName"))
            }
            res.onError { err ->
                _uiState.update { it.copy(isSwitchingWorkspace = false, errorMessage = err.message) }
            }
        }
    }

    fun toggleTwoFactor() {
        viewModelScope.launch {
            val res = toggleTwoFactorUseCase()
            res.onSuccess { updated ->
                _uiState.update { it.copy(profile = updated) }
                val status = if (updated.twoFactorEnabled) "enabled" else "disabled"
                _events.emit(ProfileEvent.ShowMessage("Two-factor authentication $status"))
            }
        }
    }

    fun openLogoutDialog() {
        _uiState.update { it.copy(showLogoutConfirmDialog = true) }
    }

    fun closeLogoutDialog() {
        _uiState.update { it.copy(showLogoutConfirmDialog = false) }
    }

    fun confirmSignOut() {
        viewModelScope.launch {
            _uiState.update { it.copy(showLogoutConfirmDialog = false) }
            _events.emit(ProfileEvent.SignedOut)
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            getUserProfileUseCase().collect { res ->
                res.onLoading { _uiState.update { it.copy(isLoading = true) } }
                res.onSuccess { p -> _uiState.update { it.copy(isLoading = false, profile = p) } }
                res.onError { err -> _uiState.update { it.copy(isLoading = false, errorMessage = err.message) } }
            }
        }
    }
}
