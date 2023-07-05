package space.webkombinat.coffeeLogger.Model.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
     private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val IS_THEME = intPreferencesKey("is_theme")
    }

    suspend fun saveTheme(num: Int) {
        dataStore.edit { preferences ->
            preferences[IS_THEME] = num
        }
    }

    val isTheme = dataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            }else {
                throw it
            }
        }
        .map { preferences ->
            preferences[IS_THEME] ?: 2
        }
}