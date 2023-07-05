package space.webkombinat.coffeeLogger.View.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@OptIn(ExperimentalTextApi::class)
@Composable
fun TempMeasure(modifier: Modifier = Modifier) {
    BoxWithConstraints {
//        val screenWidth = with(LocalDensity.current) { constraints.maxWidth.toDp() }
        val screenHeight = with(LocalDensity.current) { constraints.maxHeight.toDp() }
        val textMeasure = rememberTextMeasurer()
        val col = if (isSystemInDarkTheme()) Color.White else Color.Black
        Canvas(
            modifier = modifier
                .width(40.dp)
                .height(screenHeight - 120.dp)
                .zIndex(2f)
//                .background(Color.Green.copy(0.3f))
        ){
            val tempStep = 10
            val minTemp = 70
            val maxTemp = 230

            val height = size.height

            val tempRange = (maxTemp - minTemp) / 10
            val oneRangeSpace = height / tempRange

            for(temp in minTemp .. maxTemp step tempStep){
                val currentTemp = (temp - minTemp) / 10

                val start = when(temp) {
                    minTemp -> {
                        Offset(x = 0f, y = height)
                    }
                    maxTemp -> {
                        Offset(x = 0f, y = 0f)
                    }
                    else -> {
                        Offset(x = 0f, y = height - (oneRangeSpace * currentTemp))
                    }
                }

                val end = when(temp) {
                    minTemp -> { Offset(x = 20f, y = height)
                    }
                    maxTemp -> { Offset(x = 20f, y = 0f)
                    }
                    else -> {
                        if (temp % 50 == 0) {
                            Offset(x = 40f, y = height - (oneRangeSpace * currentTemp))
                        } else {
                            Offset(x = 20f, y = height - (oneRangeSpace * currentTemp))
                        }
                    }
                }

//              メモリ線の描画
                drawLine(
                    color = col,
                    start = start,
                    end = end,
                    strokeWidth = 2.5f
                )

//              メモリテキストの描画
                if(temp % 50 == 0) {
                    drawText(
                        textMeasurer = textMeasure,
                        text = "$temp",
                        style = TextStyle(
                            fontSize = 10.sp,
                            textAlign = TextAlign.Start,
                            color = col
                        ),
                        topLeft = Offset(50f, y = height - (oneRangeSpace * currentTemp) - 22f)
                    )
                }

            }
        }
    }
}