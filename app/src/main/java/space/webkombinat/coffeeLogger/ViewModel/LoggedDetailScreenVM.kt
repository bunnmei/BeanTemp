package space.webkombinat.coffeeLogger.ViewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import space.webkombinat.coffeeLogger.Model.db.RoastAndPoint
import space.webkombinat.coffeeLogger.Model.db.roastProfile.RoastProfileEntity
import space.webkombinat.coffeeLogger.Model.db.roastProfile.RoastProfileRepository
import javax.inject.Inject

@HiltViewModel
class LoggedDetailScreenVM @Inject constructor(
    private val repo: RoastProfileRepository
): ViewModel() {

    var data : Flow<List<RoastAndPoint>>? = null

    private val _edit = MutableStateFlow(false)
    val edit = _edit

    private val _name = MutableStateFlow("")
    val name = _name

    private val _desc = MutableStateFlow("")
    val desc = _desc

    private val _tag1Limit = MutableStateFlow(TagTime(0,0))
    private val _tag1 = MutableStateFlow(TagTime(0,0))
    val tag1 = _tag1

    private val _tag2Limit = MutableStateFlow(TagTime(0,0))
    private val _tag2 = MutableStateFlow(TagTime(0,0))
    val tag2 = _tag2

    fun setData(id: String){
        val idToNum = id.toIntOrNull()
        Log.i("console", "setData0000")
        Log.i("console", "setData$idToNum")

        if (idToNum != null) {
            Log.i("console", "setData0.50.50.5")

            viewModelScope.launch(Dispatchers.IO) {
                data = null
                data = repo.roastAndPoint(id = idToNum)
                val currentData = data?.firstOrNull()
                Log.i("console", "setData1111")
                if (currentData != null) {
                    Log.i("console", "setData2222")
                    val prof =  currentData[0].profile
                    _name.value = prof.name ?: ""
                    _desc.value = prof.description ?: ""

                    val tag1 = calc_crack_time(prof.first_crack!!)
                    val tag2 = calc_crack_time(prof.second_crack!!)
                    _tag1.value = TagTime(tag1?.get(0) ?: 0, tag1?.get(1) ?: 0)
                    _tag1Limit.value = TagTime(tag1?.get(0) ?: 0, tag1?.get(1) ?: 0)
                    _tag2.value = TagTime(tag2?.get(0) ?: 0, tag2?.get(1) ?: 0)
                    _tag2Limit.value = TagTime(tag1?.get(0) ?: 0, tag1?.get(1) ?: 0)
                }
            }
        }
    }

    fun setUpdateData() {
        if(data != null) {

        }
    }

    fun updateProfile() {
        if (data != null) {
            viewModelScope.launch(Dispatchers.IO) {
                Log.i("console", "setData")
                val currentDataList = data?.firstOrNull()
                val currentData = currentDataList?.get(0)?.profile
                if (currentData != null) {
                    val newData =
                        currentData.copy(
                            name = _name.value,
                            description = _desc.value,
                        )
                    repo.updateProfile(newData)
                }
//                data!!.take(1).collect{
//                    updateData = it[0].profile.copy(name = _name.value, description = _desc.value)
//                    repo.updateProfile(updateData!!)
//                    Log.i("console", "updated")
//                }
            }
        }
    }

    fun setEditMode(flag: Boolean){
        _edit.value = !flag
    }

    fun setName(name: String) {
        _name.value = name
    }
    fun setDesc(desc: String) {
        _desc.value = desc
    }


}

fun calc_crack_time(crackPosition: Float): List<Int>? {
    if (crackPosition == 0.0f){
        return  null
    } else {
        val time = (crackPosition / 5).toInt()
        return listOf((time / 60 - 1),(time % 60))
//            return "${(time / 60 - 1).toString().padStart(2, '0')} : ${(time % 60).toString().padStart(2, '0')}"
    }
}

data class TagTime(
    val Minute: Int,
    val Second: Int,
)