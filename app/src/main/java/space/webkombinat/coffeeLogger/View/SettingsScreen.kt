package space.webkombinat.coffeeLogger.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import space.webkombinat.coffeeLogger.View.components.SettingItemClickPanel
import space.webkombinat.coffeeLogger.View.components.SettingItemPanel
import space.webkombinat.coffeeLogger.ViewModel.SettingsScreenVM

@Composable
fun SettingsScreen(modifier: Modifier = Modifier, vm: SettingsScreenVM) {
    val checked = vm.checkedId.collectAsState().value.mode

    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        SettingItemPanel("テーマ"){
            SettingItemClickPanel(title = "システム", subTitle = "設定の状態に合わせます", panelId = 0, id = checked) {
                vm.setCheckedId(0)
            }
            SettingItemClickPanel(title = "ダーク", subTitle = "暗めのテーマ", panelId = 1, id = checked) {
                vm.setCheckedId(1)
            }
            SettingItemClickPanel(title = "ライト", subTitle = "明るいテーマ(デフォルト)", panelId = 2, id = checked) {
                vm.setCheckedId(2)
            }
        }


    }
}