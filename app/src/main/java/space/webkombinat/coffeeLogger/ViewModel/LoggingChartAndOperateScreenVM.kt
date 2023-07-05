package space.webkombinat.coffeeLogger.ViewModel

import android.hardware.usb.UsbManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.hoho.android.usbserial.util.SerialInputOutputManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import space.webkombinat.coffeeLogger.Model.db.profilePoint.ProfilePointEntity
import space.webkombinat.coffeeLogger.Model.db.profilePoint.ProfilePointRepository
import space.webkombinat.coffeeLogger.Model.db.roastProfile.RoastProfileEntity
import space.webkombinat.coffeeLogger.Model.db.roastProfile.RoastProfileRepository
import space.webkombinat.coffeeLogger.View.CrackTag
import java.io.IOException
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class LoggingChartAndOperateScreenVM @Inject constructor(
    private val roastRepo: RoastProfileRepository,
    private val pointRepo: ProfilePointRepository
): ViewModel() {

    private var _min = MutableStateFlow(4)
    val min = _min

    private var _opeState = MutableStateFlow(OpeState.INIT)
    val opeState = _opeState

//  ----USB---USB----
//    private val usbManager = application.getSystemService(Context.USB_SERVICE) as UsbManager
    private var mSerialIoManager: SerialInputOutputManager? = null
    private var usbSerialPort : UsbSerialPort? = null
    private var mListener: SerialInputOutputManager.Listener? = null
    private var _receiving = MutableStateFlow(false)
    val receiving = _receiving
    private val _temp = MutableStateFlow(0)
    val temp = _temp
//  ----USB---USB----

//  ====Timer===Timer====
    private var time: Duration = Duration.ZERO
    private lateinit var timer: Timer
    var seconds by mutableStateOf("00")
    var minutes by mutableStateOf("00")
    private var _isStarted = MutableStateFlow(false)
    var isStarted = _isStarted
//  ====Timer===Timer====

//  ++++Chart+++Chart++++
    private var _chartList = MutableStateFlow(mutableListOf(0))
    var charList = _chartList.asStateFlow()

    private var _startedArraySize = MutableStateFlow(0)
    var startedArraySize = _startedArraySize

    private val _crack1  = MutableStateFlow(CrackTag(
            crackBool = false,
            tagPosition = 0f
        ))
    var crack1 = _crack1
    private val _crack2 = MutableStateFlow(CrackTag(
            crackBool = false,
            tagPosition = 0f
        ))
    var crack2 = _crack2
//  ++++Chart+++Chart++++

//  DB
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDate():String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm")
        return current.format(formatter)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun insertLog() {
        val newProfile = RoastProfileEntity(
            name = null,
            description = null,
            first_crack = _crack1.value.tagPosition,
            second_crack = _crack2.value.tagPosition,
            create_at = getDate(),
            created_position = _startedArraySize.value
        )
        Log.i("console","dbテスト")
        Log.i("console","${newProfile}")

        viewModelScope.launch(Dispatchers.IO) {
            val id = roastRepo.insertProfile(newProfile)
            Log.i("console","${newProfile}")

            _chartList.value.forEachIndexed{ index, num ->
//                Log.i("console", "${index}")
                val newPoint = ProfilePointEntity(
                    roast_profile_id = id.toInt(),
                    point_index = index,
                    been_temp = num
                )
                pointRepo.insertPoint(newPoint)
            }
            Log.i("console", "keepAllData")
        }
    }

    //  ----USB---USB----
    fun connect(usbManager: UsbManager){
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
        if (availableDrivers.isEmpty()){
            return
        }
        val driver = availableDrivers[0]
        val connection = usbManager.openDevice(driver.device) ?: return
        usbSerialPort = driver.ports[0]
        usbSerialPort?.open(connection)
        usbSerialPort?.setParameters(9600,8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE )

        mListener = object : SerialInputOutputManager.Listener {
            override fun onNewData(data: ByteArray?) {
                if (data !=null){
                    val receive = String(data, 0, data.size).toIntOrNull()
                    Log.i("console", "${receive}")
                    if (receive != null){
                        Log.i("console", "---${receive}---")
                        _temp.value = receive
                        if(!_isStarted.value){
                            chartArrayPopAndAdd()
                        }
                    }
                }
            }
            override fun onRunError(e: Exception?) {
                Log.i("console", "なんかエラー")
                disConnect()
            }
        }
        _receiving.value = true
        startSerial()
    }

    private fun startSerial() {
        if (usbSerialPort != null){
            mSerialIoManager = SerialInputOutputManager(usbSerialPort, mListener)
            Thread(mSerialIoManager).start()
        }
    }

    fun disConnect(){
        mSerialIoManager?.stop()
        mSerialIoManager = null

        try {
            usbSerialPort?.close()
            usbSerialPort = null
        } catch (e: IOException){

        }
        mListener = null
        _receiving.value = false
    }
    //  ----USB---USB----

    //  ====Timer===Timer====
    fun startOrStop(){
        if (_isStarted.value){
            stop()
        } else {
            start()
        }
    }

    private fun start(){
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            time = time.plus(1.seconds)
            updateTimeStates()
        }
        _startedArraySize.value = _chartList.value.size
        _isStarted.value = true
        _opeState.value = OpeState.STARTED
    }

    private fun updateTimeStates() {
        chartArrayPopAndAdd()
        time.toComponents { _, minutes, seconds, _ ->
            if(minutes + 2 == _min.value){
                _min.value += 1
            }
            this@LoggingChartAndOperateScreenVM.seconds = seconds.pad()
            this@LoggingChartAndOperateScreenVM.minutes = minutes.pad()
        }
    }

    private fun chartArrayPopAndAdd(){
        _chartList.value.add(_temp.value)
        if(!_isStarted.value && _chartList.value.size > 60){
            _chartList.value.removeAt(0)
        }
    }

    private fun Int.pad(): String {
        return this.toString().padStart(2, '0')
    }

    private fun stop(){
        timer.cancel()
        _opeState.value = OpeState.STOP
//        _isStarted.value = false
    }

    fun reset() {
        time = Duration.ZERO
        resetChart()
        _min.value = 4
        _isStarted.value = false
        crack1.value = CrackTag(
            crackBool = false,
            tagPosition = 0f
        )
        crack2.value = CrackTag(
            crackBool = false,
            tagPosition = 0f
        )
        seconds = "00"
        minutes = "00"
        opeState.value = OpeState.INIT
    }
    private fun resetChart(){
        val totalSize = _chartList.value.size
        Log.i("console", "${totalSize}")
        if (totalSize >= 60) {
               _chartList.value.subList(0, (totalSize - 60)).clear()
//           for (i in 0..totalSize - 60) {
//               Log.i("console", "${i}")
//           }
        }
    }
    //  ====Timer===Timer====

    // /////markTag///////////
    fun markTag(number: Int){
        if (_isStarted.value){
            val s = seconds.toIntOrNull()
            val m = minutes.toIntOrNull()
            if (s != null && m != null) {
                val currentTime = s + (m * 60)
                val posi = 300f + (currentTime * 5f)
                if (number == 1) {
                    _crack1.value = CrackTag(crackBool = true, tagPosition = posi)
                } else {
                    _crack2.value = CrackTag(crackBool = true, tagPosition = posi)
                }
            }
        }
    }

    fun deleteTag(number: Int){
        if (number == 1){
            _crack1.value = CrackTag(crackBool = false, tagPosition = 0.0f)
        } else {
            _crack2.value = CrackTag(crackBool = false, tagPosition = 0.0f)
        }
    }
}

enum class OpeState {
    INIT,
    STARTED,
    STOP,
}