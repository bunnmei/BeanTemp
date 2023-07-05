package space.webkombinat.coffeeLogger.Model.db.roastProfile

import kotlinx.coroutines.flow.Flow
import space.webkombinat.coffeeLogger.Model.db.RoastAndPoint
import space.webkombinat.coffeeLogger.Model.db.profilePoint.ProfilePointEntity

class RoastProfileRepository(
    private val roastProfileDao: RoastProfileDao
) {

    fun roastAndPoint(id: Int): Flow<List<RoastAndPoint>> {
        return roastProfileDao.roastAndPoint(id)
    }

    fun allProfile(): Flow<List<RoastProfileEntity>> {
        return roastProfileDao.allProfile()
    }

    suspend fun insertProfile(profile: RoastProfileEntity): Long {
        return roastProfileDao.insert(profile)
    }

    suspend fun updateProfile(profile: RoastProfileEntity) {
        roastProfileDao.update(profile)
    }

    suspend fun deleteProfile(profile: RoastProfileEntity) {
        roastProfileDao.delete(profile)
    }
}