package space.webkombinat.coffeeLogger.View.components

import android.hardware.usb.UsbManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.webkombinat.coffeeLogger.R
import space.webkombinat.coffeeLogger.ViewModel.LoggingChartAndOperateScreenVM

@Composable
fun LogStatus(
    modifier: Modifier = Modifier,
    vm: LoggingChartAndOperateScreenVM,
//    temp: Int,
//    time: String,
//    isReceive: Boolean,
    usbConnect: () -> Unit
) {
    val temp = vm.temp.collectAsState().value
    val time = "${vm.minutes}:${vm.seconds}"
    val isReceive = vm.receiving.collectAsState().value
    val context = LocalContext.current
    val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
    UsbSystemBroadcastReceiver(ACTION_USB_PERMISSION){ intent ->
        if(intent?.action == UsbManager.ACTION_USB_DEVICE_ATTACHED){
            Toast.makeText(context,"なんか繋がった", Toast.LENGTH_LONG).show()
        }
        if(intent?.action == UsbManager.ACTION_USB_DEVICE_DETACHED){
            Toast.makeText(context,"なんか外れた", Toast.LENGTH_LONG).show()
        }
    }

    Row(
        modifier = modifier
            .height(60.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inverseOnSurface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = modifier.width(8.dp))
        Row(
            modifier = modifier
                .height(44.dp)
                .background(
                    MaterialTheme.colorScheme.secondaryContainer,
                    RoundedCornerShape(16.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = modifier.width(8.dp))
            Text(
                text = "豆: ${temp}",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = modifier.width(8.dp))
        }
        Spacer(modifier = modifier.width(8.dp))
        Row(
            modifier = modifier
                .height(44.dp)
                .background(
                    MaterialTheme.colorScheme.tertiaryContainer,
                    RoundedCornerShape(16.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = modifier.width(8.dp))
            Text(
                text = "経過: $time",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Spacer(modifier = modifier.width(8.dp))
        }

        Spacer(modifier = modifier.weight(1f))
        Row(
            modifier = modifier
                .height(60.dp)
                .width(60.dp)
                .clickable {
                    usbConnect()
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_usb_24),
                contentDescription = "USB-Status",
                modifier = modifier
                    .width(26.dp)
                    .height(25.dp),
                tint = if (isReceive) {
                    Color.Blue.copy(0.7f)
                } else {
                    if (isSystemInDarkTheme()){
                        Color.White
                    } else {
                        Color.Black
                    }
                }
            )
        }
    }
}