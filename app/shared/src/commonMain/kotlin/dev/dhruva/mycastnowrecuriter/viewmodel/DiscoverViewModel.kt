package dev.dhruva.mycastnowrecuriter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.dhruva.mycastnowrecuriter.domain.usecase.GetCreatorsUseCase
import dev.dhruva.mycastnowrecuriter.domain.usecase.ToggleBookmarkCreatorUseCase
import dev.dhruva.mycastnowrecuriter.model.Creator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DiscoverUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "All talent",
    val categories: List<String> = listOf("All talent", "👗 Fashion & Beauty", "📱 UGC Creators", "🎬 Actors & Models", "⚡ Fitness"),
    val creators: List<Creator> = emptyList(),
    val savedCreatorIds: Set<String> = emptySet(),
    val talentFilter: dev.dhruva.mycastnowrecuriter.model.TalentFilter = dev.dhruva.mycastnowrecuriter.model.TalentFilter(),
    val isFilterSheetOpen: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface DiscoverEvent {
    data class NavigateToProfile(val creator: Creator) : DiscoverEvent
    data class ShowMessage(val message: String) : DiscoverEvent
}

class DiscoverViewModel(
    private val getCreatorsUseCase: GetCreatorsUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkCreatorUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState(isLoading = true))
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<DiscoverEvent>()
    val events: SharedFlow<DiscoverEvent> = _events.asSharedFlow()

    init {
        loadCreators()
        observeSavedCreators()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        loadCreators()
    }

    fun onCategorySelected(category: String) {
        val cleanCat = category.replace(Regex("[^a-zA-Z &]"), "").trim()
        _uiState.update { it.copy(selectedCategory = cleanCat) }
        loadCreators()
    }

    fun openFilterSheet() {
        _uiState.update { it.copy(isFilterSheetOpen = true) }
    }

    fun closeFilterSheet() {
        _uiState.update { it.copy(isFilterSheetOpen = false) }
    }

    fun applyFilter(filter: dev.dhruva.mycastnowrecuriter.model.TalentFilter) {
        _uiState.update { it.copy(talentFilter = filter, isFilterSheetOpen = false) }
        loadCreators()
    }

    fun resetFilter() {
        _uiState.update { it.copy(talentFilter = dev.dhruva.mycastnowrecuriter.model.TalentFilter(), isFilterSheetOpen = false) }
        loadCreators()
    }

    fun onToggleBookmark(creatorId: String) {
        viewModelScope.launch {
            toggleBookmarkUseCase(creatorId).collect { resource ->
                resource.onSuccess { isSaved ->
                    val msg = if (isSaved) "Saved to your shortlisted creators" else "Removed from shortlist"
                    _events.emit(DiscoverEvent.ShowMessage(msg))
                }
            }
        }
    }

    fun onCreatorClicked(creator: Creator) {
        viewModelScope.launch {
            _events.emit(DiscoverEvent.NavigateToProfile(creator))
        }
    }

    fun refresh() {
        loadCreators()
    }

    private fun loadCreators() {
        val query = _uiState.value.searchQuery
        val cat = _uiState.value.selectedCategory

        viewModelScope.launch {
            getCreatorsUseCase(category = cat, query = query).collect { resource ->
                resource.onLoading {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                }
                resource.onSuccess { list ->
                    _uiState.update { it.copy(isLoading = false, creators = list, errorMessage = null) }
                }
                resource.onError { err ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = err.message) }
                }
            }
        }
    }

    private fun observeSavedCreators() {
        viewModelScope.launch {
            toggleBookmarkUseCase.observeSavedIds().collect { ids ->
                _uiState.update { it.copy(savedCreatorIds = ids) }
            }
        }
    }
}
