package space.webkombinat.coffeeLogger.ViewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import space.webkombinat.coffeeLogger.Model.db.roastProfile.RoastProfileRepository
import javax.inject.Inject

@HiltViewModel
class LoggedListScreenVM@Inject constructor(
    roastRepo: RoastProfileRepository
): ViewModel(){
    val allRoastProfile = roastRepo.allProfile()
}