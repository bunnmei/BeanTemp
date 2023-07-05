package space.webkombinat.coffeeLogger.Model.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

sealed class NavRouterData {
    sealed class BottomNav(
        val route: String,
        val icon: ImageVector?,
        val label: String
    ) {
        object LogList: BottomNav(route = "logList", icon = Icons.Default.List, label = "LOG")
        object ChartAndOpe: BottomNav(route = "chartAndOpe", icon = null, label = "CHART")
        object Setting: BottomNav(route = "setting", icon = Icons.Default.Settings, label = "SETTINGS")
    }

    sealed class LogListNest(
        val route: String,
        val args: String?
    ){
        object ListTop: LogListNest(route = "listTop", args = null)
        object Detail: LogListNest(route = "listDetail", args = "id")
    }
}

//fun test() {
//    val icon = DrawableCompat.wrap(ContextCompat.getDrawable(this,R.drawable.chart))
//}