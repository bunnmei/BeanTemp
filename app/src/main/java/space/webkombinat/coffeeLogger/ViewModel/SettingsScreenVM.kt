package space.webkombinat.coffeeLogger.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import space.webkombinat.coffeeLogger.Model.data.UserPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class SettingsScreenVM @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    val checkedId = userPreferencesRepository.isTheme.map {
        DisplayUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = DisplayUiState()
    )

    fun setCheckedId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.saveTheme(num = id)
        }
    }
}

data class DisplayUiState(
    val mode: Int = 2
)