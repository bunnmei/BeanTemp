package space.webkombinat.coffeeLogger.View.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import space.webkombinat.coffeeLogger.View.CrackTag
import space.webkombinat.coffeeLogger.ViewModel.LoggingChartAndOperateScreenVM
import space.webkombinat.coffeeLogger.ViewModel.calc_crack_time

@SuppressLint("SuspiciousIndentation")
@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    vm: LoggingChartAndOperateScreenVM,
    ) {
    val startedSize = vm.startedArraySize.collectAsState().value
    val isStarted = vm.isStarted.collectAsState().value
    val min = vm.min.collectAsState().value
    val tempList = vm.charList.collectAsState().value
    val crackTag1 = vm.crack1.collectAsState().value
    val crackTag2 = vm.crack2.collectAsState().value

//    val time = tempList.size
//    val pos = (crackTag1.tagPosition / 5)

    ChartCanvas(
        startedSize = startedSize,
        isStarted = isStarted,
        min = min,
        tempList = tempList,
        crackTag1 = crackTag1,
        crackTag2 = crackTag2
    )

}

@OptIn(ExperimentalTextApi::class)
@Composable
fun ChartCanvas(
    startedSize: Int,
    isStarted: Boolean,
    min: Int,
    tempList: MutableList<Int>,
    crackTag1: CrackTag,
    crackTag2: CrackTag,
){
    val textMeasure = rememberTextMeasurer()
    //min分ぶんを表示　1分当たり300f分の幅　100f時間メモリ線画が画面右に張り付くのが気持ち悪いため
    val memoryPx = min * 300f + 100f
    val canvasDp =  with(LocalDensity.current) { memoryPx.toDp()}
//    Log.i("console", "REComposeCharts")
    BoxWithConstraints {

        val lineCol = if (isSystemInDarkTheme()) Color.Cyan else Color.Blue.copy(0.8f)
//      val screenWidth = with(LocalDensity.current) { constraints.maxWidth.toDp() }
        val screenHeight = with(LocalDensity.current) { constraints.maxHeight.toDp() }
        val col =  if (isSystemInDarkTheme()){Color.White}else{Color.Black}
        Canvas(
            modifier = Modifier
                .height(screenHeight - 120.dp)
                .width(canvasDp)
//                .background(Color.Blue.copy(0.5f))
                .zIndex(2f)
        ) {
            val height = size.height
//           val allTime = 16
            val oneMinutesRange = 60f * 5f
            for (minutes in 1..min) {
                val start = Offset(x = oneMinutesRange * minutes, y = height)
                val end = Offset(x = oneMinutesRange * minutes, y = height - 20f)
                val minutesToString = (minutes - 1).toString().padStart(2, '0')
//          時間メモリ線描画
                drawLine(
                    color = col,
                    start = start,
                    end = end,
                    strokeWidth = 2.5f
                )
//          時間テキスト描画
                drawText(
                    textMeasurer = textMeasure,
                    text = minutesToString,
                    style = TextStyle(
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        color = col
                    ),
                    topLeft = Offset(x = oneMinutesRange * minutes - 17f, y = height - 60f)
                )

//          線グラフの描画
                val path = Path()
                val oneMemTemp = height / 160
                tempList.drop(1).forEachIndexed { index, i ->
                    val x = if(tempList.size <= 60 && !isStarted) {
                        (index + 1) * 5f + (300f - (tempList.size * 5f))
                    } else if(startedSize <= 60) {
                        (index + 1) * 5f + (300f - (startedSize * 5f))
                    } else {
                        (index + 1) * 5f
                    }
                    val y = height - ((i - 70f) * oneMemTemp)
//          最初の点の位置を移動するため
                    if (index == 0) {
                        path.moveTo(x = x, y = y)
                    }
                    path.lineTo(x = x, y = y)
                }

                drawPath(
                    path = path,
                    color = lineCol,
                    style = Stroke(width = 3f),
                )
//              Log.i("console", "${tempList.size}--${crackTag1.tagPosition.toInt() / 5}--$startedSize")
                if (crackTag1.crackBool) {
                    tag(color = col,
                        height = height,
                        position = crackTag1.tagPosition,
                        textMeasurer = textMeasure,
                        temp = tempList[(crackTag1.tagPosition / 5).toInt() - (60 - startedSize) - 1].toString().padStart(3, '0')
                    )
                }
                if (crackTag2.crackBool) {
                    tag(color = col,
                        height = height,
                        position = crackTag2.tagPosition,
                        textMeasurer= textMeasure,
                        temp = tempList[(crackTag2.tagPosition / 5).toInt() - (60 - startedSize) - 1].toString().padStart(3, '0')
                    )
                }

            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
fun DrawScope.tag(color: Color, height: Float, position: Float, textMeasurer: TextMeasurer,temp: String){
    drawText(
        textMeasurer = textMeasurer,
        text = "${calc_crack_time(crackPosition = position)}-$temp℃",
        style = TextStyle(
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = color
        ),
        topLeft = Offset(x = position - 110f, y = 10f)
    )

    drawLine(
        color = color,
        start = Offset(x = position,y = 60f),
        end = Offset(x = position,y = height -60f),
        strokeWidth = 2.5f
    )

    val tag = Path()
    tag.moveTo(x = position - 20f, y = height - 60f)
    tag.lineTo(x = position - 20f, y = height - 110f)
    tag.lineTo(x = position, y = height - 130f)
    tag.lineTo(x = position + 20f, y = height - 110f)
    tag.lineTo(x = position + 20f, y = height - 60f)
    tag.moveTo(x = position - 20f, y = height - 60f)

    drawPath(
        path = tag,
        color = Color.Green.copy(0.7f)
    )
}

//fun culc_temp(startedSize: Int, list: MutableList<Int>, position: Float): String{
//    val time = (position / 5).toInt() + startedSize
//    Log.i("console", "${time} --- ${list[time]}")
//    return  "${list[time]}℃"
//}

