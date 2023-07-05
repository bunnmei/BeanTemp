package space.webkombinat.coffeeLogger.View

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import space.webkombinat.coffeeLogger.Model.data.NavRouterData
import space.webkombinat.coffeeLogger.ViewModel.LoggedDetailScreenVM
import space.webkombinat.coffeeLogger.ViewModel.LoggedListScreenVM
import space.webkombinat.coffeeLogger.ViewModel.LoggingChartAndOperateScreenVM
import space.webkombinat.coffeeLogger.ViewModel.SettingsScreenVM


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen (){
    val navController = rememberNavController()
    val backStackEntry =
        navController.currentBackStackEntryAsState()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                items = listOf(
                    NavRouterData.BottomNav.LogList,
                    NavRouterData.BottomNav.ChartAndOpe,
                    NavRouterData.BottomNav.Setting
                )
            ){
                navController.navigate(it.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    ){
        NavHost(navController = navController, startDestination = NavRouterData.BottomNav.LogList.route){
//        NavHost(navController = navController, startDestination = NavRouterData.BottomNav.ChartAndOpe.route){
            loggedGraph(navController = navController)
            composable(route = NavRouterData.BottomNav.ChartAndOpe.route){
                val vm = hiltViewModel<LoggingChartAndOperateScreenVM>()
                LoggingChartAndOperateScreen(vm = vm)
            }
            composable(route = NavRouterData.BottomNav.Setting.route){
                val settingVM = hiltViewModel<SettingsScreenVM>()
                SettingsScreen(vm = settingVM)
            }
        }
    }
}

fun NavGraphBuilder.loggedGraph(navController: NavController){
    navigation(
//        startDestination = NavRouterData.LogListNest.Detail.route,
        startDestination = NavRouterData.LogListNest.ListTop.route,
        route = NavRouterData.BottomNav.LogList.route

    ) {
        composable(route = NavRouterData.LogListNest.ListTop.route){
            val vm = hiltViewModel<LoggedListScreenVM>()
            LoggedListScreen(vm = vm){ num ->
                navController.navigate(
                    route = "${NavRouterData.LogListNest.Detail.route}/$num"
                )
            }
        }
        composable(route = "${NavRouterData.LogListNest.Detail.route}/{${NavRouterData.LogListNest.Detail.args}}"){
            val vm = hiltViewModel<LoggedDetailScreenVM>()
            LoggedDetailScreen(vm=vm,id = it.arguments?.getString(NavRouterData.LogListNest.Detail.args) ?: "1")
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<NavRouterData.BottomNav>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (NavRouterData.BottomNav) -> Unit
){
    var selectedItem by remember { mutableStateOf(0) }
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar(
        modifier = modifier.height(60.dp),
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        items.forEachIndexed{ index, item ->
            NavigationBarItem(
                icon = { Icon(
                        imageVector = item.icon ?: Icons.Default.AreaChart,
                        contentDescription = item.label
                    ) },
                selected = selectedItem == index,
//                label = { Text(text = item.label)},
//                alwaysShowLabel = false,
                onClick = {
                    selectedItem = index
                    onItemClick(item)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor= MaterialTheme.colorScheme.primary,
                    indicatorColor= MaterialTheme.colorScheme.secondaryContainer,
                    unselectedIconColor= MaterialTheme.colorScheme.outline,
                    unselectedTextColor= MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}