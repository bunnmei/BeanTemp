package space.webkombinat.coffeeLogger.View


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import space.webkombinat.coffeeLogger.View.components.ChartCanvas
import space.webkombinat.coffeeLogger.View.components.LogEdit
import space.webkombinat.coffeeLogger.View.components.TempMeasure
import space.webkombinat.coffeeLogger.ViewModel.LoggedDetailScreenVM

@Composable
fun LoggedDetailScreen(modifier: Modifier = Modifier, vm: LoggedDetailScreenVM, id: String) {
    val toggle = rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        Log.i("console", "LaunchedEffectが呼び出されたよ")
        vm.setData(id)
    }
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        TopTab(toggle = toggle)
        if (!toggle.value){
            ChartLog(vm = vm)
            Box(modifier = modifier
                .fillMaxSize()
                .offset(0.dp, 60.dp)
            ){
                TempMeasure()
            }
        }else{
            Column(
                modifier = modifier
                    .offset(0.dp, 60.dp)
                    .verticalScroll(rememberScrollState())
            ){
                LogEdit(vm = vm)   
                Spacer(modifier = modifier.height(60.dp))
            }
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .offset((-16).dp, (-80).dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                if (!vm.edit.collectAsState().value) {
                    FloatingActionButton(onClick = {
                        vm.setUpdateData()
                        vm.setEditMode(vm.edit.value)
                    }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "edit"
                            )
                    }
                } else {
                    Row() {
                        FloatingActionButton(onClick = {
                            vm.updateProfile()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "edit"
                            )
                        }
                        Spacer(modifier = modifier.width(16.dp))
                        FloatingActionButton(onClick = {
                            vm.setUpdateData()
                            vm.setEditMode(vm.edit.value)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "edit"
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun ChartLog(vm: LoggedDetailScreenVM, modifier: Modifier = Modifier) {
    val data = vm.data?.collectAsState(initial = emptyList())
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if(data != null && data.value.isNotEmpty()) {
            val array = mutableListOf<Int>()
            val min = if(data.value[0].point.size / 5 < 4) 4 else data.value[0].point.size / 60 + 2
            val startSize = data.value[0].profile.created_position
            val crack1 = createCrack(data.value[0].profile.first_crack)
            val crack2 = createCrack(data.value[0].profile.second_crack)

            data.value[0].point.sortedBy { it.point_index }.forEach {
                array.add(it.been_temp)
            }

            Box(
                modifier = modifier
                    .fillMaxHeight()
                    .width(3000.dp)
                    .offset(0.dp, 60.dp)
                    .horizontalScroll(rememberScrollState()),
            ){
                ChartCanvas(
                    startedSize = startSize,
                    isStarted = true,
                    min = min,
                    tempList = array,
                    crackTag1 = crack1,
                    crackTag2 = crack2
                )
            }
        }
    }
}

fun createCrack(crack: Float?): CrackTag{
    return if (crack == 0.0f) {
        CrackTag(
            crackBool = false,
            tagPosition = 0f
        )
    } else {
        CrackTag(
            crackBool = true,
            tagPosition = crack ?: 0.0f
        )
    }
}

@Composable
fun TopTab(modifier: Modifier = Modifier, toggle: MutableState<Boolean>) {
    Row(modifier = modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {

        Column(
            modifier = modifier
                .height(60.dp)
                .weight(1f)
                .clickable {
                    toggle.value = false
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Chart",
                color = if (!toggle.value) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline
            )
        }

        Column(
            modifier = modifier
                .height(60.dp)
                .weight(1f)
                .clickable {
                    toggle.value = true
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Desc",
                color = if (toggle.value) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline
            )
        }

    }
}