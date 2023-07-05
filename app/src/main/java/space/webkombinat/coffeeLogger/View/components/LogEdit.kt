package space.webkombinat.coffeeLogger.View.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import space.webkombinat.coffeeLogger.ViewModel.LoggedDetailScreenVM
import space.webkombinat.coffeeLogger.ViewModel.calc_crack_time

@Composable
fun LogEdit(vm : LoggedDetailScreenVM, modifier: Modifier = Modifier) {
    val data = vm.data?.collectAsState(initial = emptyList())
    val flag = vm.edit.collectAsState().value

    if(data != null && data.value.isNotEmpty()) {
            if (!flag){
                data.value.forEach{
                    val time1 =  calc_crack_time(crackPosition = it.profile.first_crack!!)
                    val time2 =  calc_crack_time(crackPosition = it.profile.second_crack!!)

                    ItemPlate(itemTitle = "name", itemText = it.profile.name)
                    ItemPlate(
                        itemTitle = "first_crack",
                        itemText = "${time1?.get(0)?.toString()?.padStart(2, '0')}:${time1?.get(1)?.toString()?.padStart(2, '0')}"
                    )
                    ItemPlate(
                        itemTitle = "second_crack",
                        itemText = "${time2?.get(0)?.toString()?.padStart(2, '0')}:${time2?.get(1)?.toString()?.padStart(2, '0')}"
                    )
                    ItemPlate(itemTitle = "create_at", itemText = it.profile.create_at)
                    ItemPlate(itemTitle = "description", itemText = it.profile.description)
                }
            } else {
                ItemPlate(itemTitle = "name", itemText = null) {
                    Spacer(modifier = modifier.height(6.dp))
                    TextField(
                        value = vm.name.collectAsState().value,
                        onValueChange = {
                             vm.setName(it)
                        },
                        modifier = modifier.padding(horizontal = 16.dp)
                    )
                }
                ItemPlate(itemTitle = "desc", itemText = null) {
                    Spacer(modifier = modifier.height(6.dp))
                    TextField(
                        value = vm.desc.collectAsState().value,
                        onValueChange = {
                            vm.setDesc(it)
                        },
                        modifier = modifier.padding(horizontal = 16.dp)
                    )
                }
                ItemPlate(itemTitle = "first crack", itemText = null) {
                    Spacer(modifier = modifier.height(6.dp))
                    Row(
                        modifier = modifier.padding(horizontal = 16.dp)
                    ) {
                        TimeSet(up = {  }, down = {  },time = vm.tag1.collectAsState().value.Minute.toString(), unit = "分")
                        TimeSet(up = {  }, down = {  },time = vm.tag1.collectAsState().value.Second.toString(), unit = "秒")
                    }
                }
                ItemPlate(itemTitle = "second crack", itemText = null) {
                    Spacer(modifier = modifier.height(6.dp))
                    Row(
                        modifier = modifier.padding(horizontal = 16.dp)
                    ) {
                        TimeSet(up = {  }, down = {  },time = vm.tag2.collectAsState().value.Minute.toString(), unit = "分")
                        TimeSet(up = {  }, down = {  },time = vm.tag2.collectAsState().value.Second.toString(), unit = "秒")
                    }
                }
            }
    }
}

