package space.webkombinat.coffeeLogger.View

import android.content.Context
import android.hardware.usb.UsbManager
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import space.webkombinat.coffeeLogger.View.components.LineChart
import space.webkombinat.coffeeLogger.View.components.LogStatus
import space.webkombinat.coffeeLogger.View.components.OperateButtons
import space.webkombinat.coffeeLogger.View.components.TempMeasure
import space.webkombinat.coffeeLogger.ViewModel.LoggingChartAndOperateScreenVM

@Composable
fun LoggingChartAndOperateScreen(
    vm: LoggingChartAndOperateScreenVM,
    modifier: Modifier = Modifier
) {
    val usbManager = remember { mutableStateOf<UsbManager?>(null) }
    val context = LocalContext.current
    LaunchedEffect(Unit){
        usbManager.value  = context.getSystemService(Context.USB_SERVICE) as? UsbManager
    }
    val isReceive = vm.receiving.collectAsState().value
    Log.i("console", "REComposeCartAndOpe")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface.copy(0.5f))
    ) {
        Box(modifier = modifier
            .fillMaxSize()
            .offset(0.dp, 60.dp)
        ){
            TempMeasure()
        }
        Box(
            modifier = modifier
                .fillMaxHeight()
                .width(3000.dp)
                .offset(0.dp, 60.dp)
                .horizontalScroll(rememberScrollState()),
        ){
//      ここからグラフ
            LineChart(
                vm = vm
            )
        }

//      ステータスバー
        Box( modifier = modifier
            .fillMaxSize()
            .offset {
                IntOffset(0, 0)
            }
        ){
            LogStatus(
                vm = vm
            ){
                if (!isReceive && usbManager.value != null){
                    vm.connect(usbManager.value!!)
                } else {
                    vm.disConnect()
                }
            }
        }
        Box(
            modifier = modifier
                .fillMaxSize()
                .offset((-16).dp, (-80).dp),
            contentAlignment = Alignment.BottomEnd
        ){
            OperateButtons(
                vm = vm
            )
        }
    }
}

data class CrackTag(
    var crackBool: Boolean,
    var tagPosition: Float
)
