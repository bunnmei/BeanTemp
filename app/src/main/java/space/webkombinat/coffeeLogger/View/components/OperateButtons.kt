package space.webkombinat.coffeeLogger.View.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import space.webkombinat.coffeeLogger.ViewModel.LoggingChartAndOperateScreenVM
import space.webkombinat.coffeeLogger.ViewModel.OpeState
import space.webkombinat.coffeeLogger.ViewModel.OpeState.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OperateButtons(
    vm: LoggingChartAndOperateScreenVM,
    modifier: Modifier = Modifier
) {
    var button by remember {
        mutableStateOf(false)
    }
    val rotateAnime by animateIntAsState(
        targetValue = if(button) 45 else 0,
        animationSpec = tween(
            durationMillis = 150,
        )
    )
    val State = vm.opeState.collectAsState().value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when(State) {
            INIT ->
                OpeBtn(button = button, icon = Icons.Default.PlayArrow, action =  { vm.startOrStop() })
            STARTED -> {
                OpeBtn(button = button, icon = Icons.Default.Done, action = { vm.markTag(1) }, longPress = { vm.deleteTag(1) })
                OpeBtn(button = button, icon = Icons.Default.DoneAll, action = { vm.markTag(2) }, longPress = { vm.deleteTag(2) })
                OpeBtn(button = button, icon = Icons.Default.Pause, action =  { vm.startOrStop() })
            }
            STOP -> {
                OpeBtn(button = button, icon = Icons.Default.Download, action = { vm.insertLog() })
                OpeBtn(button = button, icon = Icons.Default.RestartAlt, action = { vm.reset() })
            }
        }

        Spacer(modifier = modifier.height(6.dp))
        FloatingActionButton(
            modifier= modifier
                .rotate(rotateAnime.toFloat()),
            onClick = {
                button = !button
            }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "btn",
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = modifier.size(24.dp)
            )
        }
    }
}


@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun OpeBtn(
    modifier: Modifier = Modifier,
    button: Boolean, icon: ImageVector,
    action: () -> Unit,
    longPress: () -> Unit = {}
) {
    AnimatedVisibility(
        visible = button,
        enter = scaleIn(animationSpec = tween(durationMillis = 150)),
        exit = scaleOut(animationSpec = tween(durationMillis = 150)),
    ) {
        IconButton(
            modifier = modifier
                .height(48.dp)
                .width(48.dp),
            onClick = {
                action()
            }
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    imageVector = icon,
                    contentDescription = "btn",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = modifier.size(24.dp)
                )
            }
        }
    }
    Spacer(modifier = modifier.height(8.dp))
}