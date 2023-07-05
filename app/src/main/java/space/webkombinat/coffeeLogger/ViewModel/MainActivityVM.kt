package space.webkombinat.coffeeLogger.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import space.webkombinat.coffeeLogger.Model.data.UserPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class MainActivityVM @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    val checkedId = userPreferencesRepository.isTheme.map {
        DisplayUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = DisplayUiState()
    )
}