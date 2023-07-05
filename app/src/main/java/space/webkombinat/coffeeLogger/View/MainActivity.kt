package space.webkombinat.coffeeLogger.View

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import space.webkombinat.coffeeLogger.View.ui.theme.CoffeeLoggerTheme
import space.webkombinat.coffeeLogger.ViewModel.LoggingChartAndOperateScreenVM
import space.webkombinat.coffeeLogger.ViewModel.MainActivityVM

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = hiltViewModel<MainActivityVM>()
            val mode = viewModel.checkedId.collectAsState().value.mode
            CoffeeLoggerTheme(
                darkTheme = when(mode){
                    1 -> true
                    2 -> false
                    else -> isSystemInDarkTheme()
                }
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationScreen()
                }
            }
        }
    }
}
